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

public class BiomeBetaTaiga {
	public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
		MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
		ModernBetaBiomeMobs.addCommonMobs(spawnSettings);
		ModernBetaBiomeMobs.addSquid(spawnSettings);
		ModernBetaBiomeMobs.addTaigaMobs(spawnSettings);

		BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);
		ModernBetaBiomeFeatures.addTaigaFeatures(genSettings, false);

		return (new Biome.BiomeBuilder())
				.hasPrecipitation(true)
				//.temperature(0.4F) TODO: Re-add this later as it looks more accurate; for some reason precipitation currently does not work properly.
				//.downfall(0.8F)
				.temperature(0.0F)
				.downfall(0.5F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						//.skyColor(8756991) TODO: Re-add when above fixed
						.skyColor(ModernBetaBiomeColors.BETA_COOL_SKY_COLOR)
						.fogColor(ModernBetaBiomeColors.BETA_FOG_COLOR)
						.waterColor(ModernBetaBiomeColors.VANILLA_FROZEN_WATER_COLOR)
						.waterFogColor(ModernBetaBiomeColors.VANILLA_FROZEN_WATER_FOG_COLOR)
						.build())
				.mobSpawnSettings(spawnSettings.build())
				.generationSettings(genSettings.build())
				.build();
	}
}
