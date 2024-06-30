package mod.bespectacled.modernbeta.world.carver;

import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.carver.WorldCarver;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModernBetaCarvers {
	public static final DeferredRegister<WorldCarver<?>> WORLD_CARVERS = DeferredRegister.create(BuiltInRegistries.CARVER, ModernBeta.MOD_ID);

	public static final Supplier<WorldCarver<BetaCaveCarverConfig>> BETA_CAVE = WORLD_CARVERS.register(
			"beta_cave",
			() -> new BetaCaveCarver(BetaCaveCarverConfig.CAVE_CODEC)
	);
}
