package mod.bespectacled.modernbeta.world.biome.biomes.beta;

import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeColors;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeFeatures;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeMobs;
import net.minecraft.core.HolderGetter;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class BiomeBetaWarmOcean {
	public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
		MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
		ModernBetaBiomeMobs.addWarmOceanMobs(spawnSettings);

		BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);
		ModernBetaBiomeFeatures.addWarmOceanFeatures(genSettings, false);

		return (new Biome.BiomeBuilder())
				.hasPrecipitation(true)
				.temperature(1.0F)
				.downfall(1.0F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.skyColor(ModernBetaBiomeColors.BETA_WARM_SKY_COLOR)
						.fogColor(ModernBetaBiomeColors.BETA_FOG_COLOR)
						.waterColor(ModernBetaBiomeColors.USE_DEBUG_OCEAN_COLOR ? 16777215 : ModernBetaBiomeColors.VANILLA_WARM_WATER_COLOR)
						.waterFogColor(ModernBetaBiomeColors.VANILLA_WARM_WATER_FOG_COLOR)
						.build())
				.mobSpawnSettings(spawnSettings.build())
				.generationSettings(genSettings.build())
				.build();
	}
}
