package mod.bespectacled.modernbeta.world.chunk;

import com.mojang.serialization.MapCodec;
import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModernBetaChunkGenerators {
	public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister.create(BuiltInRegistries.CHUNK_GENERATOR, ModernBeta.MOD_ID);

	public static final Supplier<MapCodec<ModernBetaChunkGenerator>> MODERN_BETA_CHUNK_GENERATOR = CHUNK_GENERATORS.register("modern_beta_chunk_generator", () -> ModernBetaChunkGenerator.CODEC);
}
