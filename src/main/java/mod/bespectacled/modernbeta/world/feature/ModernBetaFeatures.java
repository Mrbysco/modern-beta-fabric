package mod.bespectacled.modernbeta.world.feature;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

@SuppressWarnings("deprecation")
public class ModernBetaFeatures {
	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE, ModernBeta.MOD_ID);


	public static final Supplier<BetaFreezeTopLayerFeature> FREEZE_TOP_LAYER = FEATURES.register(
			ModernBetaFeatureTags.FREEZE_TOP_LAYER, () -> new BetaFreezeTopLayerFeature(NoneFeatureConfiguration.CODEC)
	);

	public static final Supplier<BetaFancyOakFeature> OLD_FANCY_OAK = FEATURES.register(
			ModernBetaFeatureTags.FANCY_OAK, () -> new BetaFancyOakFeature(NoneFeatureConfiguration.CODEC)
	);

	public static final Supplier<BetaOreClayFeature> ORE_CLAY = FEATURES.register(
			ModernBetaFeatureTags.ORE_CLAY, () -> new BetaOreClayFeature(OreConfiguration.CODEC)
	);
}