package mod.bespectacled.modernbeta.world.feature.placed;

import mod.bespectacled.modernbeta.world.feature.ModernBetaFeatureTags;
import mod.bespectacled.modernbeta.world.feature.configured.ModernBetaOreConfiguredFeatures;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class ModernBetaOrePlacedFeatures {
	public static final ResourceKey<PlacedFeature> ORE_CLAY = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.ORE_CLAY);
	public static final ResourceKey<PlacedFeature> ORE_EMERALD_Y95 = ModernBetaPlacedFeatures.of(ModernBetaFeatureTags.ORE_EMERALD_Y95);

	public static void bootstrap(BootstrapContext<PlacedFeature> featureRegisterable) {
		HolderGetter<ConfiguredFeature<?, ?>> registryConfigured = featureRegisterable.lookup(Registries.CONFIGURED_FEATURE);

		Holder.Reference<ConfiguredFeature<?, ?>> oreClay = registryConfigured.getOrThrow(ModernBetaOreConfiguredFeatures.ORE_CLAY);
		Holder.Reference<ConfiguredFeature<?, ?>> oreEmeraldY95 = registryConfigured.getOrThrow(ModernBetaOreConfiguredFeatures.ORE_EMERALD_Y95);

		PlacementUtils.register(featureRegisterable, ORE_CLAY, oreClay, modifiersWithCount(33, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(127))));
		PlacementUtils.register(featureRegisterable, ORE_EMERALD_Y95, oreEmeraldY95, modifiersWithCount(11, HeightRangePlacement.uniform(VerticalAnchor.absolute(95), VerticalAnchor.top())));
	}

	private static List<PlacementModifier> modifiers(PlacementModifier first, PlacementModifier second) {
		return List.of(first, InSquarePlacement.spread(), second, BiomeFilter.biome());
	}

	private static List<PlacementModifier> modifiersWithCount(int count, PlacementModifier modifier) {
		return modifiers(CountPlacement.of(count), modifier);
	}
}
