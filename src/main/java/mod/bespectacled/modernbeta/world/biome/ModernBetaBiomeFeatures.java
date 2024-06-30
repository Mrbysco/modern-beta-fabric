package mod.bespectacled.modernbeta.world.biome;

import mod.bespectacled.modernbeta.world.carver.configured.ModernBetaConfiguredCarvers;
import mod.bespectacled.modernbeta.world.feature.placed.ModernBetaMiscPlacedFeatures;
import mod.bespectacled.modernbeta.world.feature.placed.ModernBetaOrePlacedFeatures;
import mod.bespectacled.modernbeta.world.feature.placed.ModernBetaVegetationPlacedFeatures;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.Carvers;
import net.minecraft.data.worldgen.placement.AquaticPlacements;
import net.minecraft.data.worldgen.placement.CavePlacements;
import net.minecraft.data.worldgen.placement.MiscOverworldPlacements;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.GenerationStep.Decoration;

public class ModernBetaBiomeFeatures {
    /*
    private static final boolean BETA_ADD_LAKES = true;
    private static final boolean BETA_ADD_SPRINGS = true;
    
    private static final boolean BETA_ADD_ALTERNATE_STONES = true;
    private static final boolean BETA_ADD_NEW_MINEABLES = true;

    private static final boolean ALPHA_ADD_LAKES = false;
    private static final boolean ALPHA_ADD_SPRINGS = true;
    
    private static final boolean ALPHA_ADD_ALTERNATE_STONES = false;
    private static final boolean ALPHA_ADD_NEW_MINEABLES = false;

    private static final boolean INFDEV_611_ADD_LAKES = false;
    private static final boolean INFDEV_611_ADD_SPRINGS = false;
    
    private static final boolean INFDEV_420_ADD_LAKES = false;
    private static final boolean INFDEV_420_ADD_SPRINGS = false;
    
    private static final boolean INFDEV_415_ADD_LAKES = false;
    private static final boolean INFDEV_415_ADD_SPRINGS = false;
    
    private static final boolean INFDEV_227_ADD_LAKES = false;
    private static final boolean INFDEV_227_ADD_SPRINGS = false;
    
    private static final boolean INDEV_ADD_LAKES = false;
    private static final boolean INDEV_ADD_SPRINGS = false;
    */

	/* Beta Biomes */

	public static void addDesertFeatures(BiomeGenerationSettings.Builder builder, boolean pe) {
		BiomeDefaultFeatures.addFossilDecoration(builder);

		addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);

		BiomeDefaultFeatures.addDesertExtraDecoration(builder);

		if (pe) {
			addPEVegetation(builder, false);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_CACTUS_PE);

			BiomeDefaultFeatures.addDefaultMushrooms(builder);
			BiomeDefaultFeatures.addDefaultExtraVegetation(builder);
		} else {
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
			builder.addFeature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_DEAD_BUSH_2);

			BiomeDefaultFeatures.addDefaultMushrooms(builder);
			BiomeDefaultFeatures.addDefaultExtraVegetation(builder);

			builder.addFeature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_CACTUS_DESERT);
		}
	}

	public static void addForestFeatures(BiomeGenerationSettings.Builder builder, boolean pe) {
		addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);

		if (pe) {
			addPEVegetation(builder, true);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_PE_FOREST);
		} else {
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_BETA_FOREST_BEES);

			builder.addFeature(Decoration.VEGETAL_DECORATION, VegetationPlacements.FOREST_FLOWERS);
			builder.addFeature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST);
		}

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder);
	}

	public static void addIceDesertFeatures(BiomeGenerationSettings.Builder builder, boolean pe) {
		addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);

		if (pe) {
			addPEVegetation(builder, false);
		} else {
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
		}

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder);
	}

	public static void addPlainsFeatures(BiomeGenerationSettings.Builder builder, boolean pe) {
		addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);

		if (pe) {
			addPEVegetation(builder, true);
		} else {
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_3);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_PLAINS_10);
		}

		builder.addFeature(Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_PLAINS);

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder);
	}

	public static void addRainforestFeatures(BiomeGenerationSettings.Builder builder, boolean pe) {
		addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);

		if (pe) {
			addPEVegetation(builder, true);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_PE_RAINFOREST);
		} else {
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_BETA_RAINFOREST_BEES);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_RAINFOREST_10);
			builder.addFeature(Decoration.VEGETAL_DECORATION, VegetationPlacements.FOREST_FLOWERS);
		}

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder);

		builder.addFeature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_MELON_SPARSE);
	}

	public static void addSavannaFeatures(BiomeGenerationSettings.Builder builder, boolean pe) {
		addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);

		if (pe) {
			addPEVegetation(builder, false);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_PE_SPARSE);
		} else {
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_BETA_SPARSE_BEES);
		}

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder);
	}

	public static void addSeasonalForestFeatures(BiomeGenerationSettings.Builder builder, boolean pe) {
		addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);

		if (pe) {
			addPEVegetation(builder, true);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_PE_SEASONAL_FOREST);
		} else {
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_4);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_BETA_SEASONAL_FOREST_BEES);
			builder.addFeature(Decoration.VEGETAL_DECORATION, VegetationPlacements.PATCH_GRASS_FOREST);
		}

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder);
	}

	public static void addShrublandFeatures(BiomeGenerationSettings.Builder builder, boolean pe) {
		addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);

		if (pe) {
			addPEVegetation(builder, false);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_PE_SPARSE);
		} else {
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_BETA_SPARSE_BEES);
		}

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder);
	}

	public static void addSkyFeatures(BiomeGenerationSettings.Builder builder) {
		addDefaultFeatures(builder, ModernBetaFeatureSettings.SKY);

		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_BETA_SPARSE);

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder);
	}

	public static void addSwamplandFeatures(BiomeGenerationSettings.Builder builder, boolean pe) {
		addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);

		if (pe) {
			addPEVegetation(builder, false);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_PE_SPARSE);
		} else {
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_BETA_SPARSE_BEES);
			builder.addFeature(Decoration.VEGETAL_DECORATION, VegetationPlacements.FLOWER_SWAMP);
			//builder.feature(Feature.VEGETAL_DECORATION, VegetationPlacedFeatures.PATCH_WATERLILY);
		}

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder);
	}

	public static void addTaigaFeatures(BiomeGenerationSettings.Builder builder, boolean pe) {
		addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);

		if (pe) {
			addPEVegetation(builder, true);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_PE_TAIGA);
		} else {
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_BETA_TAIGA);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_TAIGA_1);
		}

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder);
		if (!pe) BiomeDefaultFeatures.addCommonBerryBushes(builder);
	}

	public static void addTundraFeatures(BiomeGenerationSettings.Builder builder, boolean pe) {
		addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);

		if (pe) {
			addPEVegetation(builder, false);
		} else {
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
		}

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder);
	}

	public static void addOceanFeatures(BiomeGenerationSettings.Builder builder, boolean pe) {
		addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);

		if (pe) {
			addPEVegetation(builder, false);
		} else {
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
		}

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder);

		if (!pe) {
			builder.addFeature(Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_NORMAL);
			builder.addFeature(Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_SIMPLE);
			builder.addFeature(Decoration.VEGETAL_DECORATION, AquaticPlacements.KELP_COLD);
		}
	}

	public static void addColdOceanFeatures(BiomeGenerationSettings.Builder builder, boolean pe) {
		addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);

		if (pe) {
			addPEVegetation(builder, false);
		} else {
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
		}

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder);

		if (!pe) {
			builder.addFeature(Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_COLD);
			builder.addFeature(Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_SIMPLE);
			builder.addFeature(Decoration.VEGETAL_DECORATION, AquaticPlacements.KELP_COLD);
		}
	}

	public static void addFrozenOceanFeatures(BiomeGenerationSettings.Builder builder, boolean pe) {
		addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);

		if (pe) {
			addPEVegetation(builder, false);
		} else {
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
		}

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder);
	}

	public static void addLukewarmOceanFeatures(BiomeGenerationSettings.Builder builder, boolean pe) {
		addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);

		if (pe) {
			addPEVegetation(builder, false);
		} else {
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
		}

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder);

		if (!pe) {
			builder.addFeature(Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_WARM);
			builder.addFeature(Decoration.VEGETAL_DECORATION, AquaticPlacements.KELP_WARM);
		}
	}

	public static void addWarmOceanFeatures(BiomeGenerationSettings.Builder builder, boolean pe) {
		addDefaultFeatures(builder, pe ? ModernBetaFeatureSettings.PE : ModernBetaFeatureSettings.BETA);

		if (pe) {
			addPEVegetation(builder, false);
		} else {
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
		}

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder);

		if (!pe) {
			builder.addFeature(Decoration.VEGETAL_DECORATION, AquaticPlacements.WARM_OCEAN_VEGETATION);
			builder.addFeature(Decoration.VEGETAL_DECORATION, AquaticPlacements.SEAGRASS_WARM);
			builder.addFeature(Decoration.VEGETAL_DECORATION, AquaticPlacements.SEA_PICKLE);
		}
	}

	/* Inf Biomes */

	public static void addAlphaFeatures(BiomeGenerationSettings.Builder builder) {
		addDefaultFeatures(builder, ModernBetaFeatureSettings.ALPHA);

		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_CACTUS_ALPHA);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_ALPHA);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
		BiomeDefaultFeatures.addDefaultExtraVegetation(builder);
	}

	public static void addInfdev611Features(BiomeGenerationSettings.Builder builder) {
		addDefaultFeatures(builder, ModernBetaFeatureSettings.INFDEV_611);

		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_INFDEV_611);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
	}

	public static void addInfdev420Features(BiomeGenerationSettings.Builder builder) {
		addDefaultFeatures(builder, ModernBetaFeatureSettings.INFDEV_420);

		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_INFDEV_420);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
	}

	public static void addInfdev415Features(BiomeGenerationSettings.Builder builder) {
		addDefaultFeatures(builder, ModernBetaFeatureSettings.INFDEV_415);

		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_INFDEV_415);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
	}

	public static void addInfdev227Features(BiomeGenerationSettings.Builder builder) {
		addDefaultFeatures(builder, ModernBetaFeatureSettings.INFDEV_227);

		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_INFDEV_227);

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
	}

	/* Indev Biomes */

	public static void addIndevHellFeatures(BiomeGenerationSettings.Builder builder) {
		addDefaultFeatures(builder, ModernBetaFeatureSettings.INDEV);

		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_INDEV);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.MUSHROOM_HELL);

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
	}

	public static void addIndevNormalFeatures(BiomeGenerationSettings.Builder builder) {
		addDefaultFeatures(builder, ModernBetaFeatureSettings.INDEV);

		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_INDEV);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
	}

	public static void addIndevParadiseFeatures(BiomeGenerationSettings.Builder builder) {
		addDefaultFeatures(builder, ModernBetaFeatureSettings.INDEV);

		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_FLOWER_PARADISE);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_INDEV);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);

		BiomeDefaultFeatures.addDefaultMushrooms(builder);
	}

	public static void addIndevSnowyFeatures(BiomeGenerationSettings.Builder builder) {
		addIndevNormalFeatures(builder);
	}

	public static void addIndevWoodsFeatures(BiomeGenerationSettings.Builder builder) {
		addDefaultFeatures(builder, ModernBetaFeatureSettings.INDEV);

		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION_2);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.TREES_INDEV_WOODS);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.MUSHROOM_HELL);
	}

	public static void addDefaultFeatures(
			BiomeGenerationSettings.Builder builder,
			ModernBetaFeatureSettings featureSettings
	) {
		addCarvers(builder, featureSettings.addCanyons);
		if (featureSettings.addLakes) addLakes(builder);
		BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
		BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
		addMineables(builder, featureSettings.addClay, featureSettings.addAlternateStones, featureSettings.addNewMineables);

		if (featureSettings.addSprings) BiomeDefaultFeatures.addDefaultSprings(builder);
		if (featureSettings.useBetaFreezeTopLayer)
			ModernBetaBiomeFeatures.addBetaFrozenTopLayer(builder);
		else
			BiomeDefaultFeatures.addSurfaceFreezing(builder);

		BiomeDefaultFeatures.addDefaultOres(builder);
		addOres(builder);
	}

	public static void addDefaultFeatures(BiomeGenerationSettings.Builder builder, boolean isOcean, boolean addLakes, boolean addSprings) {
		if (addLakes) addLakes(builder);
		BiomeDefaultFeatures.addDefaultMonsterRoom(builder);
		BiomeDefaultFeatures.addDefaultOres(builder);
		if (addSprings) BiomeDefaultFeatures.addDefaultSprings(builder);
		BiomeDefaultFeatures.addDefaultCrystalFormations(builder);
	}

	private static void addCarvers(BiomeGenerationSettings.Builder builder, boolean addCanyons) {
		builder.addCarver(GenerationStep.Carving.AIR, ModernBetaConfiguredCarvers.BETA_CAVE);
		builder.addCarver(GenerationStep.Carving.AIR, ModernBetaConfiguredCarvers.BETA_CAVE_DEEP);

		if (addCanyons) {
			builder.addCarver(GenerationStep.Carving.AIR, Carvers.CANYON);
		}
	}

	private static void addLakes(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(GenerationStep.Decoration.LAKES, MiscOverworldPlacements.LAKE_LAVA_UNDERGROUND);
		builder.addFeature(GenerationStep.Decoration.LAKES, MiscOverworldPlacements.LAKE_LAVA_SURFACE);
	}

	private static void addMineables(BiomeGenerationSettings.Builder builder, boolean addClay, boolean addAlternateStones, boolean addNewMineables) {
		builder.addFeature(Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIRT);
		builder.addFeature(Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GRAVEL);

		if (addClay) {
			builder.addFeature(Decoration.UNDERGROUND_ORES, ModernBetaOrePlacedFeatures.ORE_CLAY);
		}

		if (addAlternateStones) {
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_GRANITE_LOWER);
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_DIORITE_LOWER);
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_ANDESITE_LOWER);
		}

		if (addNewMineables) {
			builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_TUFF);
			builder.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, CavePlacements.GLOW_LICHEN);
		}
	}

	private static void addOres(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(Decoration.UNDERGROUND_ORES, ModernBetaOrePlacedFeatures.ORE_EMERALD_Y95);
	}

	private static void addBetaFrozenTopLayer(BiomeGenerationSettings.Builder builder) {
		builder.addFeature(Decoration.TOP_LAYER_MODIFICATION, ModernBetaMiscPlacedFeatures.FREEZE_TOP_LAYER);
	}

	private static void addPEVegetation(BiomeGenerationSettings.Builder builder, boolean addGrass) {
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_DANDELION);
		builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_POPPY);

		if (addGrass)
			builder.addFeature(Decoration.VEGETAL_DECORATION, ModernBetaVegetationPlacedFeatures.PATCH_GRASS_ALPHA_2);
	}

	private record ModernBetaFeatureSettings(
			boolean addCanyons,
			boolean addLakes,
			boolean addSprings,
			boolean addClay,
			boolean addAlternateStones,
			boolean addNewMineables,
			boolean useBetaFreezeTopLayer
	) {
		private ModernBetaFeatureSettings(boolean setting) {
			this(setting, setting, setting, setting, setting, setting, setting);
		}

		private static final ModernBetaFeatureSettings BETA = new ModernBetaFeatureSettings(true);
		private static final ModernBetaFeatureSettings PE = new ModernBetaFeatureSettings(true, false, true, true, false, true, true);
		private static final ModernBetaFeatureSettings SKY = new ModernBetaFeatureSettings(false, true, true, true, false, false, true);
		private static final ModernBetaFeatureSettings ALPHA = new ModernBetaFeatureSettings(false, false, true, true, false, false, false);
		private static final ModernBetaFeatureSettings INFDEV_611 = new ModernBetaFeatureSettings(false);
		private static final ModernBetaFeatureSettings INFDEV_420 = new ModernBetaFeatureSettings(false);
		private static final ModernBetaFeatureSettings INFDEV_415 = new ModernBetaFeatureSettings(false);
		private static final ModernBetaFeatureSettings INFDEV_227 = new ModernBetaFeatureSettings(false);
		private static final ModernBetaFeatureSettings INDEV = new ModernBetaFeatureSettings(false);
	}
}
