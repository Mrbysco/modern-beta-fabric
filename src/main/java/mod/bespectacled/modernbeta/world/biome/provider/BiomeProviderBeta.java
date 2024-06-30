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
import mod.bespectacled.modernbeta.util.chunk.ChunkClimateSky;
import mod.bespectacled.modernbeta.util.noise.SimplexOctaveNoise;
import mod.bespectacled.modernbeta.world.biome.provider.climate.ClimateMap;
import mod.bespectacled.modernbeta.world.biome.provider.climate.ClimateType;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class BiomeProviderBeta extends BiomeProvider implements ClimateSampler, ClimateSamplerSky, BiomeResolverBlock, BiomeResolverOcean {
	private final ClimateMap climateMap;
	private final BetaClimateSampler climateSampler;
	private final BetaClimateSamplerSky climateSamplerSky;

	public BiomeProviderBeta(CompoundTag settings, HolderGetter<Biome> biomeRegistry, long seed) {
		super(settings, biomeRegistry, seed);

		this.climateMap = new ClimateMap(this.settings);
		this.climateSampler = new BetaClimateSampler(
				this.seed,
				this.settings.climateTempNoiseScale,
				this.settings.climateRainNoiseScale,
				this.settings.climateDetailNoiseScale
		);
		this.climateSamplerSky = new BetaClimateSamplerSky(
				this.seed,
				this.settings.climateTempNoiseScale
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
				.map(key -> this.biomeRegistry.getOrThrow(key))
				.collect(Collectors.toList());
	}

	@Override
	public double sampleSky(int x, int z) {
		return this.climateSamplerSky.sample(x, z);
	}

	@Override
	public Clime sample(int x, int z) {
		return this.climateSampler.sample(x, z);
	}

	@Override
	public boolean useBiomeColor() {
		return ModernBetaConfig.COMMON.useBetaBiomeColor.get();
	}

	@Override
	public boolean useSkyColor() {
		return ModernBetaConfig.COMMON.useBetaSkyColor.get();
	}

	@Override
	public boolean useWaterColor() {
		return ModernBetaConfig.COMMON.useBetaWaterColor.get();
	}

	private static class BetaClimateSampler {
		private final SimplexOctaveNoise tempOctaveNoise;
		private final SimplexOctaveNoise rainOctaveNoise;
		private final SimplexOctaveNoise detailOctaveNoise;

		private final ChunkCache<ChunkClimate> chunkCacheClimate;

		private final double tempNoiseScale;
		private final double rainNoiseScale;
		private final double detailNoiseScale;

		public BetaClimateSampler(long seed, double tempNoiseScale, double rainNoiseScale, double detailNoiseScale) {
			this.tempOctaveNoise = new SimplexOctaveNoise(new Random(seed * 9871L), 4);
			this.rainOctaveNoise = new SimplexOctaveNoise(new Random(seed * 39811L), 4);
			this.detailOctaveNoise = new SimplexOctaveNoise(new Random(seed * 543321L), 2);

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

		public Clime sampleNoise(int x, int z) {
			double temp = this.tempOctaveNoise.sample(x, z, this.tempNoiseScale, 0.25D);
			double rain = this.rainOctaveNoise.sample(x, z, this.rainNoiseScale, 0.33333333333333331D);
			double detail = this.detailOctaveNoise.sample(x, z, this.detailNoiseScale, 0.58823529411764708D);

			detail = detail * 1.1D + 0.5D;

			temp = (temp * 0.15D + 0.7D) * 0.99D + detail * 0.01D;
			rain = (rain * 0.15D + 0.5D) * 0.998D + detail * 0.002D;

			temp = 1.0D - (1.0D - temp) * (1.0D - temp);

			return new Clime(Mth.clamp(temp, 0.0, 1.0), Mth.clamp(rain, 0.0, 1.0));
		}
	}

	private static class BetaClimateSamplerSky {
		private final SimplexOctaveNoise tempOctaveNoise;

		private final ChunkCache<ChunkClimateSky> chunkCacheClimateSky;

		private final double tempNoiseScale;

		public BetaClimateSamplerSky(long seed, double tempNoiseScale) {
			this.tempOctaveNoise = new SimplexOctaveNoise(new Random(seed * 9871L), 4);

			this.chunkCacheClimateSky = new ChunkCache<>(
					"sky",
					(chunkX, chunkZ) -> new ChunkClimateSky(chunkX, chunkZ, this::sampleNoise)
			);

			this.tempNoiseScale = tempNoiseScale;
		}

		public double sample(int x, int z) {
			int chunkX = x >> 4;
			int chunkZ = z >> 4;

			return this.chunkCacheClimateSky.get(chunkX, chunkZ).sampleTemp(x, z);
		}

		private double sampleNoise(int x, int z) {
			return this.tempOctaveNoise.sample(x, z, this.tempNoiseScale, this.tempNoiseScale, 0.5D);
		}
	}
}
