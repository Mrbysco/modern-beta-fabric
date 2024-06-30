package mod.bespectacled.modernbeta.world.feature.configured;

import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatureTags;
import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatures;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public class ModernBetaTreeConfiguredFeatures {
	public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_OAK = ModernBetaConfiguredFeatures.of(ModernBetaFeatureTags.FANCY_OAK);

	@SuppressWarnings("unchecked")
	public static void bootstrap(BootstrapContext<?> registerable) {
		BootstrapContext<ConfiguredFeature<?, ?>> featureRegisterable = (BootstrapContext<ConfiguredFeature<?, ?>>) registerable;

		FeatureUtils.register(featureRegisterable, FANCY_OAK, ModernBetaFeatures.OLD_FANCY_OAK.get(), FeatureConfiguration.NONE);
	}
}   
