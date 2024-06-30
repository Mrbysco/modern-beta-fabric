package mod.bespectacled.modernbeta.world.feature.placed;

import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatureTags;
import mod.bespectacled.modernbeta.world.feature.configured.ModernBetaTreeConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModernBetaTreePlacedFeatures {
	public static final ResourceKey<PlacedFeature> FANCY_OAK = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.FANCY_OAK);

	public static void bootstrap(BootstrapContext<PlacedFeature> featureRegisterable) {
		HolderGetter<ConfiguredFeature<?, ?>> registryConfigured = featureRegisterable.lookup(Registries.CONFIGURED_FEATURE);

		Holder.Reference<ConfiguredFeature<?, ?>> fancyOak = registryConfigured.getOrThrow(ModernBetaTreeConfiguredFeatures.FANCY_OAK);

		PlacementUtils.register(featureRegisterable, FANCY_OAK, fancyOak, PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING));
	}
}
