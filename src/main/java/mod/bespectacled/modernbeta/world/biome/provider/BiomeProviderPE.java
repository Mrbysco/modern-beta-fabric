package mod.bespectacled.modernbeta.world.biome.provider;

import mod.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import mod.bespectacled.modernbeta.api.world.biome.BiomeResolverBlock;
import mod.bespectacled.modernbeta.api.world.biome.BiomeResolverOcean;
import mod.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import mod.bespectacled.modernbeta.api.world.biome.climate.ClimateSamplerSky;
import mod.bespectacled.modernbeta.api.world.biome.climate.Clime;
import mod.bespectacled.modernbeta.config.ModernBetaConfig;
import mod.bespectacled.modernbeta.util.chunk.ChunkCache;
import mod.bespectacled.modernbeta.util.chunk.ChunkClimate;
import mod.bespectacled.modernbeta.util.mersenne.MTRandom;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.world.biome.provider.climate.ClimateMap;
import mod.bespectacled.modernbeta.world.biome.provider.climate.ClimateType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.stream.Collectors;

public class BiomeProviderPE extends BiomeProvider implements ClimateSampler, ClimateSamplerSky, BiomeResolverBlock, BiomeResolverOcean {
	private final ClimateMap climateMap;
	private final PEClimateSampler climateSampler;

	public BiomeProviderPE(CompoundTag settings, HolderGetter<Biome> biomeRegistry, long seed) {
		super(settings, biomeRegistry, seed);

		this.climateMap = new ClimateMap(this.settings);
		this.climateSampler = new PEClimateSampler(
				this.seed,
				this.settings.climateTempNoiseScale,
				this.settings.climateRainNoiseScale,
				this.settings.climateDetailNoiseScale
		);
	}

	@Override
	public Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
		int x = biomeX << 2;
		int z = biomeZ << 2;

		Clime clime = this.climateSampler.sample(x, z);
		double temp = clime.temp();
		double rain = clime.rain();

		return this.biomeRegistry.getOrThrow(this.climateMap.getBiome(temp, rain, ClimateType.LAND));
	}

	@Override
	public Holder<Biome> getOceanBiome(int biomeX, int biomeY, int biomeZ) {
		int x = biomeX << 2;
		int z = biomeZ << 2;

		Clime clime = this.climateSampler.sample(x, z);
		double temp = clime.temp();
		double rain = clime.rain();

		return this.biomeRegistry.getOrThrow(this.climateMap.getBiome(temp, rain, ClimateType.OCEAN));
	}

	@Override
	public Holder<Biome> getDeepOceanBiome(int biomeX, int biomeY, int biomeZ) {
		int x = biomeX << 2;
		int z = biomeZ << 2;

		Clime clime = this.climateSampler.sample(x, z);
		double temp = clime.temp();
		double rain = clime.rain();

		return this.biomeRegistry.getOrThrow(this.climateMap.getBiome(temp, rain, ClimateType.DEEP_OCEAN));
	}

	@Override
	public Holder<Biome> getBiomeBlock(int x, int y, int z) {
		Clime clime = this.climateSampler.sample(x, z);
		double temp = clime.temp();
		double rain = clime.rain();

		return this.biomeRegistry.getOrThrow(this.climateMap.getBiome(temp, rain, ClimateType.LAND));
	}

	@Override
	public List<Holder<Biome>> getBiomes() {
		return this.climateMap
				.getBiomeKeys()
				.stream()
				.map(i -> this.biomeRegistry.getOrThrow(i))
				.collect(Collectors.toList());
	}

	@Override
	public double sampleSky(int x, int z) {
		return this.climateSampler.sampleSky(x, z);
	}

	@Override
	public Clime sample(int x, int z) {
		return this.climateSampler.sample(x, z);
	}

	@Override
	public boolean useBiomeColor() {
		return ModernBetaConfig.COMMON.usePEBetaBiomeColor.get();
	}

	@Override
	public boolean useSkyColor() {
		return ModernBetaConfig.COMMON.usePEBetaSkyColor.get();
	}

	@Override
	public boolean useWaterColor() {
		return ModernBetaConfig.COMMON.usePEBetaWaterColor.get();
	}

	private static class PEClimateSampler {
		private final PerlinOctaveNoise tempOctaveNoise;
		private final PerlinOctaveNoise rainOctaveNoise;
		private final PerlinOctaveNoise detailOctaveNoise;

		private final ChunkCache<ChunkClimate> chunkCacheClimate;

		private final double tempNoiseScale;
		private final double rainNoiseScale;
		private final double detailNoiseScale;

		public PEClimateSampler(long seed, double tempNoiseScale, double rainNoiseScale, double detailNoiseScale) {
			this.tempOctaveNoise = new PerlinOctaveNoise(new MTRandom(seed * 9871L), 4, true);
			this.rainOctaveNoise = new PerlinOctaveNoise(new MTRandom(seed * 39811L), 4, true);
			this.detailOctaveNoise = new PerlinOctaveNoise(new MTRandom(seed * 543321L), 2, true);

			this.chunkCacheClimate = new ChunkCache<>(
					"climate",
					(chunkX, chunkZ) -> new ChunkClimate(chunkX, chunkZ, this::sampleNoise)
			);

			this.tempNoiseScale = tempNoiseScale;
			this.rainNoiseScale = rainNoiseScale;
			this.detailNoiseScale = detailNoiseScale;
		}

		public Clime sample(int x, int z) {
			int chunkX = x >> 4;
			int chunkZ = z >> 4;

			return this.chunkCacheClimate.get(chunkX, chunkZ).sampleClime(x, z);
		}

		public double sampleSky(int x, int z) {
			int chunkX = x >> 4;
			int chunkZ = z >> 4;

			return this.chunkCacheClimate.get(chunkX, chunkZ).sampleClime(x, z).temp();
		}

		private Clime sampleNoise(int x, int z) {
			double temp = this.tempOctaveNoise.sampleXZ(x, z, this.tempNoiseScale, this.tempNoiseScale);
			double rain = this.rainOctaveNoise.sampleXZ(x, z, this.rainNoiseScale, this.rainNoiseScale);
			double detail = this.detailOctaveNoise.sampleXZ(x, z, this.detailNoiseScale, this.detailNoiseScale);

			detail = detail * 1.1D + 0.5D;

			temp = (temp * 0.15D + 0.7D) * 0.99D + detail * 0.01D;
			rain = (rain * 0.15D + 0.5D) * 0.998D + detail * 0.002D;

			temp = 1.0D - (1.0D - temp) * (1.0D - temp);

			return new Clime(Mth.clamp(temp, 0.0, 1.0), Mth.clamp(rain, 0.0, 1.0));
		}
	}
}
