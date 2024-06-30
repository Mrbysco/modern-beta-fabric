package mod.bespectacled.modernbeta.world.chunk;

import mod.bespectacled.modernbeta.api.world.chunk.ChunkProvider;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProviderNoise;
import mod.bespectacled.modernbeta.util.chunk.ChunkHeightmap;
import mod.bespectacled.modernbeta.util.noise.SimpleDensityFunction;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Aquifer.FluidPicker;
import net.minecraft.world.level.levelgen.DensityFunctions;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;

public class ModernBetaChunkNoiseSampler extends NoiseChunk {
	private static final int OCEAN_HEIGHT_OFFSET = -8;

	private final ChunkProvider chunkProvider;

	public static ModernBetaChunkNoiseSampler create(
			ChunkAccess chunk,
			RandomState noiseConfig,
			NoiseGeneratorSettings chunkGeneratorSettings,
			Aquifer.FluidPicker fluidLevelSampler,
			ChunkProvider chunkProvider
	) {
		NoiseSettings shapeConfig = chunkGeneratorSettings.noiseSettings().clampToHeightAccessor(chunk);
		ChunkPos chunkPos = chunk.getPos();

		int horizontalSize = 16 / shapeConfig.getCellWidth();

		return new ModernBetaChunkNoiseSampler(
				horizontalSize,
				noiseConfig,
				chunkPos.getMinBlockX(),
				chunkPos.getMinBlockZ(),
				shapeConfig,
				SimpleDensityFunction.INSTANCE,
				chunkGeneratorSettings,
				fluidLevelSampler,
				Blender.empty(),
				chunkProvider
		);
	}

	private ModernBetaChunkNoiseSampler(
			int horizontalSize,
			RandomState noiseConfig,
			int startX,
			int startZ,
			NoiseSettings shapeConfig,
			DensityFunctions.BeardifierOrMarker beardifying,
			NoiseGeneratorSettings settings,
			FluidPicker fluidLevelSampler,
			Blender blender,
			ChunkProvider chunkProvider
	) {
		super(
				horizontalSize,
				noiseConfig,
				startX,
				startZ,
				shapeConfig,
				beardifying,
				settings,
				fluidLevelSampler,
				blender
		);

		this.chunkProvider = chunkProvider;
	}

	/*
	 * Simulates a general y height at x/z block coordinates.
	 * Replace vanilla noise implementation with plain height sampling.
	 *
	 * Used to determine whether an aquifer should use sea level or local water level.
	 * Also used in SurfaceBuilder to determine min surface y.
	 *
	 * Reference: https://twitter.com/henrikkniberg/status/1432615996880310274
	 *
	 */
	@Override
	public int preliminarySurfaceLevel(int x, int z) {
		int height = (this.chunkProvider instanceof ChunkProviderNoise noiseChunkProvider) ?
				noiseChunkProvider.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR) :
				this.chunkProvider.getHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG);

		int seaLevel = this.chunkProvider.getSeaLevel();

		// Fudge deeper oceans when at a body of water
		return (height < seaLevel) ? height + OCEAN_HEIGHT_OFFSET : height;
	}
}
