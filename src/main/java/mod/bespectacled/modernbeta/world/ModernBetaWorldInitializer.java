package mod.bespectacled.modernbeta.world;

import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.dimension.LevelStem;

public class ModernBetaWorldInitializer {
	public static void init(MinecraftServer server) {
		Registry<LevelStem> registryDimensionOptions = server.registryAccess().registryOrThrow(Registries.LEVEL_STEM);
		long seed = server.getWorldData().worldGenOptions().seed();

		registryDimensionOptions.entrySet().forEach(entry -> {
			LevelStem dimensionOptions = entry.getValue();

			ChunkGenerator chunkGenerator = dimensionOptions.generator();
			BiomeSource biomeSource = chunkGenerator.getBiomeSource();

			if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
				modernBetaChunkGenerator.initProvider(seed);
			}

			if (biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
				modernBetaBiomeSource.initProvider(seed);
			}
		});
	}
}
