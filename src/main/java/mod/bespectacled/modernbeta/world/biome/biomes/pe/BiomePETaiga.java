package mod.bespectacled.modernbeta.world.biome.biomes.pe;

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

public class BiomePETaiga {
	public static Biome create(HolderGetter<PlacedFeature> featureLookup, HolderGetter<ConfiguredWorldCarver<?>> carverLookup) {
		MobSpawnSettings.Builder spawnSettings = new MobSpawnSettings.Builder();
		ModernBetaBiomeMobs.addCommonMobs(spawnSettings);
		ModernBetaBiomeMobs.addSquid(spawnSettings);
		ModernBetaBiomeMobs.addTaigaMobs(spawnSettings);

		BiomeGenerationSettings.Builder genSettings = new BiomeGenerationSettings.Builder(featureLookup, carverLookup);
		ModernBetaBiomeFeatures.addTaigaFeatures(genSettings, true);

		return (new Biome.BiomeBuilder())
				.hasPrecipitation(true)
				//.temperature(0.4F) TODO: Re-add this later as it looks more accurate; for some reason precipitation currently does not work properly.
				//.downfall(0.8F)
				.temperature(0.0F)
				.downfall(0.5F)
				.specialEffects((new BiomeSpecialEffects.Builder())
						//.skyColor(8756991) TODO: Re-add when above fixed
						.skyColor(ModernBetaBiomeColors.PE_SKY_COLOR)
						.fogColor(ModernBetaBiomeColors.PE_FOG_COLOR)
						.waterColor(ModernBetaBiomeColors.OLD_WATER_COLOR)
						.waterFogColor(ModernBetaBiomeColors.OLD_WATER_FOG_COLOR)
						.grassColorOverride(ModernBetaBiomeColors.PE_GRASS_COLOR)
						.foliageColorOverride(ModernBetaBiomeColors.PE_FOLIAGE_COLOR)
						.build())
				.mobSpawnSettings(spawnSettings.build())
				.generationSettings(genSettings.build())
				.build();
	}
}
