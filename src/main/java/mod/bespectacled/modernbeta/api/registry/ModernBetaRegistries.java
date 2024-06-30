package mod.bespectacled.modernbeta.api.registry;

import mod.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import mod.bespectacled.modernbeta.api.world.blocksource.BlockSource;
import mod.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProvider;
import mod.bespectacled.modernbeta.api.world.chunk.noise.NoisePostProcessor;
import mod.bespectacled.modernbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsChunk;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsPreset;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;

public final class ModernBetaRegistries {
	public static final ModernBetaRegistry<ChunkProviderCreator> CHUNK;
	public static final ModernBetaRegistry<BiomeProviderCreator> BIOME;
	public static final ModernBetaRegistry<CaveBiomeProviderCreator> CAVE_BIOME;
	public static final ModernBetaRegistry<NoisePostProcessor> NOISE_POST_PROCESSOR;
	public static final ModernBetaRegistry<SurfaceConfig> SURFACE_CONFIG;
	public static final ModernBetaRegistry<BlockSourceCreator> BLOCKSOURCE;
	public static final ModernBetaRegistry<ModernBetaSettingsPreset> SETTINGS_PRESET;
	public static final ModernBetaRegistry<ModernBetaSettingsPreset> SETTINGS_PRESET_ALT;

	static {
		CHUNK = new ModernBetaRegistry<>("CHUNK");
		BIOME = new ModernBetaRegistry<>("BIOME");
		CAVE_BIOME = new ModernBetaRegistry<>("CAVE_BIOME");
		NOISE_POST_PROCESSOR = new ModernBetaRegistry<>("NOISE_POST_PROCESSOR");
		SURFACE_CONFIG = new ModernBetaRegistry<>("SURFACE_CONFIG");
		BLOCKSOURCE = new ModernBetaRegistry<>("BLOCKSOURCE");
		SETTINGS_PRESET = new ModernBetaRegistry<>("SETTINGS_PRESET");
		SETTINGS_PRESET_ALT = new ModernBetaRegistry<>("SETTINGS_PRESET_ALT");
	}

	@FunctionalInterface
	public static interface ChunkProviderCreator {
		ChunkProvider apply(ModernBetaChunkGenerator chunkGenerator, long seed);
	}

	@FunctionalInterface
	public static interface BiomeProviderCreator {
		BiomeProvider apply(CompoundTag settings, HolderGetter<Biome> biomeRegistry, long seed);
	}

	@FunctionalInterface
	public static interface CaveBiomeProviderCreator {
		CaveBiomeProvider apply(CompoundTag settings, HolderGetter<Biome> biomeRegistry, long seed);
	}

	@FunctionalInterface
	public static interface BlockSourceCreator {
		BlockSource apply(ModernBetaSettingsChunk settingsChunk, PositionalRandomFactory randomSplitter);
	}
}
