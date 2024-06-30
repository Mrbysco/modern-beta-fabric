package mod.bespectacled.modernbeta.world.feature.placement;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModernBetaPlacementTypes<P extends PlacementModifier> {
	public static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES = DeferredRegister.create(BuiltInRegistries.PLACEMENT_MODIFIER_TYPE, ModernBeta.MOD_ID);

	public static final Supplier<PlacementModifierType<NoiseBasedCountPlacementModifierBeta>> BETA_NOISE_BASED_COUNT = register("beta_noise_based_count", () -> () -> NoiseBasedCountPlacementModifierBeta.MODIFIER_CODEC);
	public static final Supplier<PlacementModifierType<NoiseBasedCountPlacementModifierAlpha>> ALPHA_NOISE_BASED_COUNT = register("alpha_noise_based_count", () -> () -> NoiseBasedCountPlacementModifierAlpha.MODIFIER_CODEC);
	public static final Supplier<PlacementModifierType<NoiseBasedCountPlacementModifierInfdev415>> INFDEV_415_NOISE_BASED_COUNT = register("infdev_415_noise_based_count", () -> () -> NoiseBasedCountPlacementModifierInfdev415.MODIFIER_CODEC);
	public static final Supplier<PlacementModifierType<NoiseBasedCountPlacementModifierInfdev420>> INFDEV_420_NOISE_BASED_COUNT = register("infdev_420_noise_based_count", () -> () -> NoiseBasedCountPlacementModifierInfdev420.MODIFIER_CODEC);
	public static final Supplier<PlacementModifierType<NoiseBasedCountPlacementModifierInfdev611>> INFDEV_611_NOISE_BASED_COUNT = register("infdev_611_noise_based_count", () -> () -> NoiseBasedCountPlacementModifierInfdev611.MODIFIER_CODEC);

	public static final Supplier<PlacementModifierType<HeightmapSpreadDoublePlacementModifier>> HEIGHTMAP_SPREAD_DOUBLE = register("heightmap_spread_double", () -> () -> HeightmapSpreadDoublePlacementModifier.MODIFIER_CODEC);

	private static <P extends PlacementModifier> Supplier<PlacementModifierType<P>> register(String id, Supplier<PlacementModifierType<P>> codec) {
		return PLACEMENT_MODIFIER_TYPES.register(id, codec);
	}
}