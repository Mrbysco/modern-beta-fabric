package mod.bespectacled.modernbeta.world.feature.configured;

import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatureTags;
import mod.bespectacled.modernbeta.world.feature.placed.ModernBetaTreePlacedFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class ModernBetaVegetationConfiguredFeatures {
	public static final ResourceKey<ConfiguredFeature<?, ?>> MUSHROOM_HELL = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.MUSHROOM_HELL);
	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_DANDELION = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.PATCH_DANDELION);
	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_POPPY = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.PATCH_POPPY);
	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_DANDELION_INFDEV_227 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.PATCH_DANDELION_INFDEV_227);
	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GRASS = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.PATCH_GRASS);
	public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_GRASS_LUSH = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.PATCH_GRASS_LUSH);

	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_ALPHA = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_ALPHA);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_611 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_611);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_420 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_420);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_415 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_415);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_227 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_227);

	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_ALPHA_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_ALPHA_BEES);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_611_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_611_BEES);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_420_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_420_BEES);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_415_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_415_BEES);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INFDEV_227_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INFDEV_227_BEES);

	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_FOREST = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_FOREST);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_RAINFOREST = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_RAINFOREST);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_SEASONAL_FOREST = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_SEASONAL_FOREST);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_SPARSE = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_SPARSE);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_TAIGA = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_TAIGA);

	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_FOREST_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_FOREST_BEES);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_RAINFOREST_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_RAINFOREST_BEES);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_SEASONAL_FOREST_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_SEASONAL_FOREST_BEES);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BETA_SPARSE_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_BETA_SPARSE_BEES);

	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_FOREST = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_FOREST);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_RAINFOREST = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_RAINFOREST);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_SEASONAL_FOREST = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_SEASONAL_FOREST);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_SPARSE = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_SPARSE);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_TAIGA = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_TAIGA);

	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_FOREST_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_FOREST_BEES);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_RAINFOREST_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_RAINFOREST_BEES);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_SEASONAL_FOREST_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_SEASONAL_FOREST_BEES);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_PE_SPARSE_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_PE_SPARSE_BEES);

	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INDEV = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INDEV);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INDEV_WOODS = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INDEV_WOODS);

	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INDEV_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INDEV_BEES);
	public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_INDEV_WOODS_BEES = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.TREES_INDEV_WOODS_BEES);

	@SuppressWarnings("unchecked")
	public static void bootstrap(BootstrapContext<?> registerable) {
		BootstrapContext<ConfiguredFeature<?, ?>> featureRegisterable = (BootstrapContext<ConfiguredFeature<?, ?>>) registerable;
		HolderGetter<PlacedFeature> registryPlaced = featureRegisterable.lookup(Registries.PLACED_FEATURE);

		FeatureUtils.register(featureRegisterable, MUSHROOM_HELL, Feature.FLOWER, ModernBetaRandomPatchConfigs.MUSHROOM_HELL);
		FeatureUtils.register(featureRegisterable, PATCH_DANDELION, Feature.FLOWER, ModernBetaRandomPatchConfigs.DANDELION_CONFIG);
		FeatureUtils.register(featureRegisterable, PATCH_POPPY, Feature.FLOWER, ModernBetaRandomPatchConfigs.POPPY_CONFIG);
		FeatureUtils.register(featureRegisterable, PATCH_DANDELION_INFDEV_227, Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.DANDELION)));
		FeatureUtils.register(featureRegisterable, PATCH_GRASS, Feature.RANDOM_PATCH, ModernBetaRandomPatchConfigs.GRASS_CONFIG);
		FeatureUtils.register(featureRegisterable, PATCH_GRASS_LUSH, Feature.RANDOM_PATCH, ModernBetaRandomPatchConfigs.LUSH_GRASS_CONFIG);

		FeatureUtils.register(featureRegisterable, TREES_ALPHA, Feature.RANDOM_SELECTOR, createDefaultRandomTreeConfig(registryPlaced, false));
		FeatureUtils.register(featureRegisterable, TREES_INFDEV_611, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
		FeatureUtils.register(featureRegisterable, TREES_INFDEV_420, Feature.RANDOM_SELECTOR, createInfdevRandomTreeConfig(registryPlaced, false));
		FeatureUtils.register(featureRegisterable, TREES_INFDEV_415, Feature.RANDOM_SELECTOR, createInfdevRandomTreeConfig(registryPlaced, false));
		FeatureUtils.register(featureRegisterable, TREES_INFDEV_227, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));

		FeatureUtils.register(featureRegisterable, TREES_ALPHA_BEES, Feature.RANDOM_SELECTOR, createDefaultRandomTreeConfig(registryPlaced, true));
		FeatureUtils.register(featureRegisterable, TREES_INFDEV_611_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
		FeatureUtils.register(featureRegisterable, TREES_INFDEV_420_BEES, Feature.RANDOM_SELECTOR, createInfdevRandomTreeConfig(registryPlaced, true));
		FeatureUtils.register(featureRegisterable, TREES_INFDEV_415_BEES, Feature.RANDOM_SELECTOR, createInfdevRandomTreeConfig(registryPlaced, true));
		FeatureUtils.register(featureRegisterable, TREES_INFDEV_227_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));

		FeatureUtils.register(featureRegisterable, TREES_BETA_FOREST, Feature.RANDOM_SELECTOR, createForestRandomTreeConfig(registryPlaced, false));
		FeatureUtils.register(featureRegisterable, TREES_BETA_RAINFOREST, Feature.RANDOM_SELECTOR, createRainforestRandomTreeConfig(registryPlaced, false));
		FeatureUtils.register(featureRegisterable, TREES_BETA_SEASONAL_FOREST, Feature.RANDOM_SELECTOR, createSeasonalForestRandomTreeConfig(registryPlaced, false));
		FeatureUtils.register(featureRegisterable, TREES_BETA_SPARSE, Feature.RANDOM_SELECTOR, createDefaultRandomTreeConfig(registryPlaced, false));
		FeatureUtils.register(featureRegisterable, TREES_BETA_TAIGA, Feature.RANDOM_SELECTOR, createTaigaRandomTreeConfig(registryPlaced));

		FeatureUtils.register(featureRegisterable, TREES_BETA_FOREST_BEES, Feature.RANDOM_SELECTOR, createForestRandomTreeConfig(registryPlaced, true));
		FeatureUtils.register(featureRegisterable, TREES_BETA_RAINFOREST_BEES, Feature.RANDOM_SELECTOR, createRainforestRandomTreeConfig(registryPlaced, true));
		FeatureUtils.register(featureRegisterable, TREES_BETA_SEASONAL_FOREST_BEES, Feature.RANDOM_SELECTOR, createSeasonalForestRandomTreeConfig(registryPlaced, true));
		FeatureUtils.register(featureRegisterable, TREES_BETA_SPARSE_BEES, Feature.RANDOM_SELECTOR, createDefaultRandomTreeConfig(registryPlaced, true));

		FeatureUtils.register(featureRegisterable, TREES_PE_FOREST, Feature.RANDOM_SELECTOR, createPEForestRandomTreeConfig(registryPlaced, false));
		FeatureUtils.register(featureRegisterable, TREES_PE_RAINFOREST, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
		FeatureUtils.register(featureRegisterable, TREES_PE_SEASONAL_FOREST, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
		FeatureUtils.register(featureRegisterable, TREES_PE_SPARSE, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
		FeatureUtils.register(featureRegisterable, TREES_PE_TAIGA, Feature.RANDOM_SELECTOR, createTaigaRandomTreeConfig(registryPlaced));

		FeatureUtils.register(featureRegisterable, TREES_PE_FOREST_BEES, Feature.RANDOM_SELECTOR, createPEForestRandomTreeConfig(registryPlaced, true));
		FeatureUtils.register(featureRegisterable, TREES_PE_RAINFOREST_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
		FeatureUtils.register(featureRegisterable, TREES_PE_SEASONAL_FOREST_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
		FeatureUtils.register(featureRegisterable, TREES_PE_SPARSE_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));

		FeatureUtils.register(featureRegisterable, TREES_INDEV, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));
		FeatureUtils.register(featureRegisterable, TREES_INDEV_WOODS, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, false));

		FeatureUtils.register(featureRegisterable, TREES_INDEV_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
		FeatureUtils.register(featureRegisterable, TREES_INDEV_WOODS_BEES, Feature.RANDOM_SELECTOR, createOakTreeConfig(registryPlaced, true));
	}

	private static RandomFeatureConfiguration createOakTreeConfig(HolderGetter<PlacedFeature> registryPlaced, boolean bees) {
		Holder.Reference<PlacedFeature> oakChecked = registryPlaced.getOrThrow(TreePlacements.OAK_CHECKED);
		Holder.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(TreePlacements.OAK_BEES_0002);

		return new RandomFeatureConfiguration(List.of(), bees ? oakBees : oakChecked);
	}

	private static RandomFeatureConfiguration createDefaultRandomTreeConfig(HolderGetter<PlacedFeature> registryPlaced, boolean bees) {
		Holder.Reference<PlacedFeature> fancyOak = registryPlaced.getOrThrow(ModernBetaTreePlacedFeatures.FANCY_OAK);
		Holder.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacements.OAK_CHECKED);
		Holder.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(TreePlacements.OAK_BEES_0002);

		return new RandomFeatureConfiguration(List.of(withChance(fancyOak, 0.1f)), bees ? oakBees : oak);
	}

	private static RandomFeatureConfiguration createInfdevRandomTreeConfig(HolderGetter<PlacedFeature> registryPlaced, boolean bees) {
		Holder.Reference<PlacedFeature> fancyOak = registryPlaced.getOrThrow(ModernBetaTreePlacedFeatures.FANCY_OAK);
		Holder.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacements.OAK_CHECKED);
		Holder.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(TreePlacements.OAK_BEES_0002);

		return new RandomFeatureConfiguration(List.of(withChance(bees ? oakBees : oak, 0.1f)), fancyOak);
	}

	private static RandomFeatureConfiguration createForestRandomTreeConfig(HolderGetter<PlacedFeature> registryPlaced, boolean bees) {
		Holder.Reference<PlacedFeature> fancyOak = registryPlaced.getOrThrow(ModernBetaTreePlacedFeatures.FANCY_OAK);
		Holder.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacements.OAK_CHECKED);
		Holder.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(TreePlacements.OAK_BEES_0002);

		Holder.Reference<PlacedFeature> birch = registryPlaced.getOrThrow(TreePlacements.BIRCH_CHECKED);
		Holder.Reference<PlacedFeature> birchBees = registryPlaced.getOrThrow(TreePlacements.BIRCH_BEES_0002_PLACED);

		return new RandomFeatureConfiguration(List.of(withChance(bees ? birchBees : birch, 0.2f), withChance(fancyOak, 0.33333334f)), bees ? oakBees : oak);
	}

	private static RandomFeatureConfiguration createRainforestRandomTreeConfig(HolderGetter<PlacedFeature> registryPlaced, boolean bees) {
		Holder.Reference<PlacedFeature> fancyOak = registryPlaced.getOrThrow(ModernBetaTreePlacedFeatures.FANCY_OAK);
		Holder.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacements.OAK_CHECKED);
		Holder.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(TreePlacements.OAK_BEES_0002);

		return new RandomFeatureConfiguration(List.of(withChance(fancyOak, 0.33333334f)), bees ? oakBees : oak);
	}

	private static RandomFeatureConfiguration createSeasonalForestRandomTreeConfig(HolderGetter<PlacedFeature> registryPlaced, boolean bees) {
		Holder.Reference<PlacedFeature> fancyOak = registryPlaced.getOrThrow(ModernBetaTreePlacedFeatures.FANCY_OAK);
		Holder.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacements.OAK_CHECKED);
		Holder.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(TreePlacements.OAK_BEES_0002);

		return new RandomFeatureConfiguration(List.of(withChance(fancyOak, 0.1f)), bees ? oakBees : oak);
	}

	private static RandomFeatureConfiguration createTaigaRandomTreeConfig(HolderGetter<PlacedFeature> registryPlaced) {
		Holder.Reference<PlacedFeature> pine = registryPlaced.getOrThrow(TreePlacements.PINE_CHECKED);
		Holder.Reference<PlacedFeature> spruce = registryPlaced.getOrThrow(TreePlacements.SPRUCE_CHECKED);

		return new RandomFeatureConfiguration(List.of(withChance(pine, 0.33333334f)), spruce);
	}

	private static RandomFeatureConfiguration createPEForestRandomTreeConfig(HolderGetter<PlacedFeature> registryPlaced, boolean bees) {
		Holder.Reference<PlacedFeature> oak = registryPlaced.getOrThrow(TreePlacements.OAK_CHECKED);
		Holder.Reference<PlacedFeature> oakBees = registryPlaced.getOrThrow(TreePlacements.OAK_BEES_0002);

		Holder.Reference<PlacedFeature> birch = registryPlaced.getOrThrow(TreePlacements.BIRCH_CHECKED);
		Holder.Reference<PlacedFeature> birchBees = registryPlaced.getOrThrow(TreePlacements.BIRCH_BEES_0002_PLACED);

		return new RandomFeatureConfiguration(List.of(withChance(bees ? birchBees : birch, 0.2f)), bees ? oak : oakBees);
	}

	private static WeightedPlacedFeature withChance(Holder<PlacedFeature> feature, float chance) {
		return new WeightedPlacedFeature(feature, chance);
	}

	private static final class ModernBetaRandomPatchConfigs {
		private static final int XZ_SPREAD = 7;
		private static final int Y_SPREAD = 3;
		private static final int TRIES = 64;
		private static final int GRASS_TRIES = 64;

		public static final Holder<PlacedFeature> DANDELION_PLACED_FEATURE;
		public static final Holder<PlacedFeature> POPPY_PLACED_FEATURE;

		public static final Holder<PlacedFeature> GRASS_FEATURE;
		public static final Holder<PlacedFeature> LUSH_GRASS_FEATURE;

		public static final Holder<PlacedFeature> MUSHROOM_HELL_FEATURE;

		public static final RandomPatchConfiguration GRASS_CONFIG;
		public static final RandomPatchConfiguration LUSH_GRASS_CONFIG;

		public static final RandomPatchConfiguration DANDELION_CONFIG;
		public static final RandomPatchConfiguration POPPY_CONFIG;

		public static final RandomPatchConfiguration MUSHROOM_HELL;

		public static RandomPatchConfiguration createRandomPatchFeatureConfig(int tries, Holder<PlacedFeature> feature) {
			return new RandomPatchConfiguration(tries, XZ_SPREAD, Y_SPREAD, feature);
		}

		private static SimpleWeightedRandomList.Builder<BlockState> pool() {
			return SimpleWeightedRandomList.<BlockState>builder();
		}

		static {
			DANDELION_PLACED_FEATURE = PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.DANDELION)));
			POPPY_PLACED_FEATURE = PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.POPPY)));

			GRASS_FEATURE = PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.SHORT_GRASS)));
			LUSH_GRASS_FEATURE = PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(pool().add(BlockStates.SHORT_GRASS, 1).add(BlockStates.FERN, 4))));

			MUSHROOM_HELL_FEATURE = PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(new WeightedStateProvider(pool().add(Blocks.BROWN_MUSHROOM.defaultBlockState(), 2).add(Blocks.RED_MUSHROOM.defaultBlockState(), 1))));

			// # of tries in Beta equivalent is 128, but here it seems to generate too much grass,
			// so keep # of tries at 64 for now.
			GRASS_CONFIG = createRandomPatchFeatureConfig(GRASS_TRIES, GRASS_FEATURE);
			LUSH_GRASS_CONFIG = createRandomPatchFeatureConfig(GRASS_TRIES, LUSH_GRASS_FEATURE);

			DANDELION_CONFIG = createRandomPatchFeatureConfig(TRIES, DANDELION_PLACED_FEATURE);
			POPPY_CONFIG = createRandomPatchFeatureConfig(TRIES, POPPY_PLACED_FEATURE);

			MUSHROOM_HELL = createRandomPatchFeatureConfig(TRIES, MUSHROOM_HELL_FEATURE);
		}
	}
}
