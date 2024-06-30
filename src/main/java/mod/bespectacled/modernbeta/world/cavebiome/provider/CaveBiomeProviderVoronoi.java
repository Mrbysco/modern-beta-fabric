package mod.bespectacled.modernbeta.world.cavebiome.provider;

import mod.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import mod.bespectacled.modernbeta.api.world.cavebiome.climate.CaveClimateSampler;
import mod.bespectacled.modernbeta.api.world.cavebiome.climate.CaveClime;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.world.biome.voronoi.VoronoiPointCaveBiome;
import mod.bespectacled.modernbeta.world.biome.voronoi.VoronoiPointRules;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class CaveBiomeProviderVoronoi extends CaveBiomeProvider implements CaveClimateSampler {
	private final VoronoiCaveClimateSampler climateSampler;
	private final VoronoiPointRules<ResourceKey<Biome>, CaveClime> rules;

	public CaveBiomeProviderVoronoi(CompoundTag settings, HolderGetter<Biome> biomeRegistry, long seed) {
		super(settings, biomeRegistry, seed);

		this.climateSampler = new VoronoiCaveClimateSampler(
				seed,
				this.settings.voronoiVerticalNoiseScale,
				this.settings.voronoiHorizontalNoiseScale,
				this.settings.voronoiDepthMinY,
				this.settings.voronoiDepthMaxY
		);
		this.rules = buildRules(this.settings.voronoiPoints);
	}

	@Override
	public Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ) {
		CaveClime clime = this.sample(biomeX, biomeY, biomeZ);
		ResourceKey<Biome> biomeKey = this.rules.calculateClosestTo(clime);

		return biomeKey == null ? null : this.biomeRegistry.getOrThrow(biomeKey);
	}

	@Override
	public List<Holder<Biome>> getBiomes() {
		return this.rules.getItems().stream().distinct().map(key -> this.biomeRegistry.getOrThrow(key)).collect(Collectors.toList());
	}

	@Override
	public CaveClime sample(int x, int y, int z) {
		return this.climateSampler.sample(x, y, z);
	}

	private static VoronoiPointRules<ResourceKey<Biome>, CaveClime> buildRules(List<VoronoiPointCaveBiome> points) {
		VoronoiPointRules.Builder<ResourceKey<Biome>, CaveClime> builder = new VoronoiPointRules.Builder<>();

		for (VoronoiPointCaveBiome point : points) {
			ResourceKey<Biome> biomeKey = point.biome().isBlank() ? null : ResourceKey.create(Registries.BIOME, ResourceLocation.tryParse(point.biome()));

			double temp = Mth.clamp(point.temp(), 0.0, 1.0);
			double rain = Mth.clamp(point.rain(), 0.0, 1.0);
			double depth = Mth.clamp(point.depth(), 0.0, 1.0);

			builder.add(biomeKey, new CaveClime(temp, rain, depth));
		}

		return builder.build();
	}

	private static class VoronoiCaveClimateSampler {
		private final PerlinOctaveNoise tempOctaveNoise;
		private final PerlinOctaveNoise rainOctaveNoise;
		private final PerlinOctaveNoise detailOctaveNoise;

		private final float verticalScale;
		private final float horizontalScale;

		private final int depthMinY;
		private final int depthMaxY;

		public VoronoiCaveClimateSampler(long seed, float verticalScale, float horizontalScale, int depthMinY, int depthMaxY) {
			this.tempOctaveNoise = new PerlinOctaveNoise(new Random(seed * 9871L), 2, true);
			this.rainOctaveNoise = new PerlinOctaveNoise(new Random(seed * 39811L), 2, true);
			this.detailOctaveNoise = new PerlinOctaveNoise(new Random(seed * 543321L), 1, true);

			this.verticalScale = verticalScale;
			this.horizontalScale = horizontalScale;

			this.depthMinY = depthMinY >> 2;
			this.depthMaxY = depthMaxY >> 2;
		}

		public CaveClime sample(int x, int y, int z) {
			// 1 Octave range: -0.6240559817912857/0.6169702737066762
			// 2 Octave range: -1.4281536012354779/1.4303502066204832
			// 4 Octave range: -7.6556244276339145/7.410194314594666

			double tempNoise = this.tempOctaveNoise.sample(
					x / (double) this.horizontalScale,
					y / (double) this.verticalScale,
					z / (double) this.horizontalScale
			);

			double rainNoise = this.rainOctaveNoise.sample(
					x / (double) this.horizontalScale,
					y / (double) this.verticalScale,
					z / (double) this.horizontalScale
			);

			double detailNoise = this.detailOctaveNoise.sample(
					x / (double) this.horizontalScale,
					y / (double) this.verticalScale,
					z / (double) this.horizontalScale
			);

			tempNoise /= 1.4D;
			rainNoise /= 1.4D;
			detailNoise /= 0.55D;

			tempNoise = tempNoise * 0.99D + detailNoise * 0.01D;
			rainNoise = rainNoise * 0.98D + detailNoise * 0.02D;

			tempNoise = (tempNoise + 1.0) / 2D;
			rainNoise = (rainNoise + 1.0) / 2D;

			int depthHeight = this.depthMaxY - this.depthMinY;
			double depth = Mth.clamp(y, this.depthMinY, this.depthMaxY);

			depth -= this.depthMinY;
			depth /= depthHeight;

			return new CaveClime(
					Mth.clamp(tempNoise, 0.0, 1.0),
					Mth.clamp(rainNoise, 0.0, 1.0),
					Mth.clamp(depth, 0.0, 1.0)
			);
		}
	}
}
