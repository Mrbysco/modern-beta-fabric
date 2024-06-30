package mod.bespectacled.modernbeta.world.feature.configured;

import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatureTags;
import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class ModernBetaOreConfiguredFeatures {
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_CLAY = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.ORE_CLAY);
	public static final ResourceKey<ConfiguredFeature<?, ?>> ORE_EMERALD_Y95 = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.ORE_EMERALD_Y95);

	@SuppressWarnings("unchecked")
	public static void bootstrap(BootstrapContext<?> registerable) {
		BootstrapContext<ConfiguredFeature<?, ?>> featureRegisterable = (BootstrapContext<ConfiguredFeature<?, ?>>) registerable;

		TagMatchTest ruleStone = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
		TagMatchTest ruleDeepslate = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);

		List<OreConfiguration.TargetBlockState> targets = List.of(
				OreConfiguration.target(ruleStone, Blocks.EMERALD_ORE.defaultBlockState()),
				OreConfiguration.target(ruleDeepslate, Blocks.DEEPSLATE_EMERALD_ORE.defaultBlockState())
		);

		FeatureUtils.register(featureRegisterable, ORE_CLAY, ModernBetaFeatures.ORE_CLAY.get(), new OreConfiguration(new BlockMatchTest(Blocks.SAND), Blocks.CLAY.defaultBlockState(), 33));
		FeatureUtils.register(featureRegisterable, ORE_EMERALD_Y95, Feature.ORE, new OreConfiguration(targets, 8, 0.9f));
	}
}
