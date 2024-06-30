package mod.bespectacled.modernbeta.world.feature.configured;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class ModernBetaConfiguredFeatures {
	public static void bootstrap(BootstrapContext<?> featureRegisterable) {
		ModernBetaMiscConfiguredFeatures.bootstrap(featureRegisterable);
		ModernBetaOreConfiguredFeatures.bootstrap(featureRegisterable);
		ModernBetaTreeConfiguredFeatures.bootstrap(featureRegisterable);
		ModernBetaVegetationConfiguredFeatures.bootstrap(featureRegisterable);
	}

	public static ResourceKey<ConfiguredFeature<?, ?>> of(String id) {
		return ResourceKey.create(Registries.CONFIGURED_FEATURE, ModernBeta.createId(id));
	}
}
