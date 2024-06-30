package mod.bespectacled.modernbeta.world.biome;

import com.mojang.serialization.MapCodec;
import mod.bespectacled.modernbeta.ModernBeta;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.biome.BiomeSource;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModernBetaBiomeSources {
	public static final DeferredRegister<MapCodec<? extends BiomeSource>> BIOME_SOURCES = DeferredRegister.create(BuiltInRegistries.BIOME_SOURCE, ModernBeta.MOD_ID);

	public static final Supplier<MapCodec<ModernBetaBiomeSource>> MODERN_BETA_BIOME_SOURCE = BIOME_SOURCES.register("modern_beta_biome_source", () -> ModernBetaBiomeSource.CODEC);
}
