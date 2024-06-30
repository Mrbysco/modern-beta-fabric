package mod.bespectacled.modernbeta.world.biome.biomes.indev;

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

public class BiomeIndevParadise {
	public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
		MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
		ModernBetaBiomeMobs.addCommonMobs(spawnSettings);
		ModernBetaBiomeMobs.addSquid(spawnSettings);

		BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);
		ModernBetaBiomeFeatures.addIndevParadiseFeatures(genSettings);

		return (new Biome.BiomeBuilder())
				.hasPrecipitation(false)
				.temperature(0.6F)
				.downfall(0.6F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						.grassColorOverride(ModernBetaBiomeColors.OLD_GRASS_COLOR)
						.foliageColorOverride(ModernBetaBiomeColors.OLD_FOLIAGE_COLOR)
						.skyColor(ModernBetaBiomeColors.INDEV_PARADISE_SKY_COLOR)
						.fogColor(ModernBetaBiomeColors.INDEV_PARADISE_FOG_COLOR)
						.waterColor(ModernBetaBiomeColors.OLD_WATER_COLOR)
						.waterFogColor(ModernBetaBiomeColors.OLD_WATER_FOG_COLOR)
						.build())
				.mobSpawnSettings(spawnSettings.build())
				.generationSettings(genSettings.build())
				.build();
	}
}
