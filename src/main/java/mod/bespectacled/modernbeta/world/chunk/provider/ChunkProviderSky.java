package mod.bespectacled.modernbeta.world.chunk.provider;

import mod.bespectacled.modernbeta.api.world.chunk.ChunkProviderNoise;
import mod.bespectacled.modernbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.RandomState;

import java.util.Random;

public class ChunkProviderSky extends ChunkProviderNoise {
	private final PerlinOctaveNoise minLimitOctaveNoise;
	private final PerlinOctaveNoise maxLimitOctaveNoise;
	private final PerlinOctaveNoise mainOctaveNoise;
	private final PerlinOctaveNoise surfaceOctaveNoise;
	private final PerlinOctaveNoise forestOctaveNoise;

	public ChunkProviderSky(ModernBetaChunkGenerator chunkGenerator, long seed) {
		super(chunkGenerator, seed);

		this.minLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
		this.maxLimitOctaveNoise = new PerlinOctaveNoise(this.random, 16, true);
		this.mainOctaveNoise = new PerlinOctaveNoise(this.random, 8, true);
		new PerlinOctaveNoise(this.random, 4, true);
		this.surfaceOctaveNoise = new PerlinOctaveNoise(this.random, 4, true);
		new PerlinOctaveNoise(this.random, 10, true);
		new PerlinOctaveNoise(this.random, 16, true);
		this.forestOctaveNoise = new PerlinOctaveNoise(this.random, 8, true);
	}

	@Override
	public void provideSurface(WorldGenRegion region, StructureManager structureAccessor, ChunkAccess chunk, ModernBetaBiomeSource biomeSource, RandomState noiseConfig) {
		double scale = 0.03125D;

		ChunkPos chunkPos = chunk.getPos();
		int chunkX = chunkPos.x;
		int chunkZ = chunkPos.z;

		int startX = chunk.getPos().getMinBlockX();
		int startZ = chunk.getPos().getMinBlockZ();

		Random rand = this.createSurfaceRandom(chunkX, chunkZ);
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		double[] surfaceNoise = surfaceOctaveNoise.sampleBeta(
				chunkX * 16, chunkZ * 16, 0.0D,
				16, 16, 1,
				scale * 2D, scale * 2D, scale * 2D
		);

		for (int localZ = 0; localZ < 16; localZ++) {
			for (int localX = 0; localX < 16; localX++) {
				int x = startX + localX;
				int z = startZ + localZ;
				int surfaceTopY = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG).getFirstAvailable(localX, localZ);

				int surfaceDepth = (int) (surfaceNoise[localZ + localX * 16] / 3D + 3D + rand.nextDouble() * 0.25D);

				int runDepth = -1;

				Holder<Biome> biome = biomeSource.getBiomeForSurfaceGen(region, pos.set(x, surfaceTopY, z));

				SurfaceConfig surfaceConfig = this.surfaceBuilder.getSurfaceConfig(biome);
				BlockState topBlock = surfaceConfig.normal().topBlock();
				BlockState fillerBlock = surfaceConfig.normal().fillerBlock();

				// Generate from top to bottom of world
				for (int y = this.worldTopY - 1; y >= this.worldMinY; y--) {
					BlockState blockState;

					pos.set(localX, y, localZ);
					blockState = chunk.getBlockState(pos);

					if (blockState.isAir()) { // Skip if air block
						runDepth = -1;
						continue;
					}

					if (!blockState.is(this.defaultBlock.getBlock())) { // Skip if not stone
						continue;
					}

					if (runDepth == -1) {
						if (surfaceDepth <= 0) { // Generate stone basin if noise permits
							topBlock = BlockStates.AIR;
							fillerBlock = this.defaultBlock;
						}

						runDepth = surfaceDepth;

						blockState = (y >= 0) ?
								topBlock :
								fillerBlock;

						chunk.setBlockState(pos, blockState, false);

						continue;
					}

					if (runDepth <= 0) {
						continue;
					}

					runDepth--;
					chunk.setBlockState(pos, fillerBlock, false);

					// Generates layer of sandstone starting at lowest block of sand, of height 1 to 4.
					if (runDepth == 0 && fillerBlock.is(Blocks.SAND)) {
						runDepth = rand.nextInt(4);
						fillerBlock = BlockStates.SANDSTONE;
					}

					if (runDepth == 0 && fillerBlock.is(Blocks.RED_SAND)) {
						runDepth = rand.nextInt(4);
						fillerBlock = BlockStates.RED_SANDSTONE;
					}
				}
			}
		}
	}

	@Override
	protected void sampleNoiseColumn(double[] primaryBuffer, double[] heightmapBuffer, int startNoiseX, int startNoiseZ, int localNoiseX, int localNoiseZ) {
		int noiseX = startNoiseX + localNoiseX;
		int noiseZ = startNoiseZ + localNoiseZ;

		double coordinateScale = this.chunkSettings.noiseCoordinateScale;
		double heightScale = this.chunkSettings.noiseHeightScale;

		double mainNoiseScaleX = this.chunkSettings.noiseMainNoiseScaleX;
		double mainNoiseScaleY = this.chunkSettings.noiseMainNoiseScaleY;
		double mainNoiseScaleZ = this.chunkSettings.noiseMainNoiseScaleZ;

		double lowerLimitScale = this.chunkSettings.noiseLowerLimitScale;
		double upperLimitScale = this.chunkSettings.noiseUpperLimitScale;

		for (int y = 0; y < primaryBuffer.length; ++y) {
			int noiseY = y + this.noiseMinY;

			double density;
			double heightmapDensity;

			double densityOffset = this.getOffset();

			// Equivalent to current MC noise.sample() function, see NoiseColumnSampler.
			double mainNoise = (this.mainOctaveNoise.sample(
					noiseX, noiseY, noiseZ,
					coordinateScale / mainNoiseScaleX,
					heightScale / mainNoiseScaleY,
					coordinateScale / mainNoiseScaleZ
			) / 10D + 1.0D) / 2D;

			if (mainNoise < 0.0D) {
				density = this.minLimitOctaveNoise.sample(
						noiseX, noiseY, noiseZ,
						coordinateScale,
						heightScale,
						coordinateScale
				) / lowerLimitScale;

			} else if (mainNoise > 1.0D) {
				density = this.maxLimitOctaveNoise.sample(
						noiseX, noiseY, noiseZ,
						coordinateScale,
						heightScale,
						coordinateScale
				) / upperLimitScale;

			} else {
				double minLimitNoise = this.minLimitOctaveNoise.sample(
						noiseX, noiseY, noiseZ,
						coordinateScale,
						heightScale,
						coordinateScale
				) / lowerLimitScale;

				double maxLimitNoise = this.maxLimitOctaveNoise.sample(
						noiseX, noiseY, noiseZ,
						coordinateScale,
						heightScale,
						coordinateScale
				) / upperLimitScale;

				density = minLimitNoise + (maxLimitNoise - minLimitNoise) * mainNoise;
			}

			density -= densityOffset;

			// Sample without noise caves
			heightmapDensity = density;

			// Sample for noise caves
			density = this.sampleNoisePostProcessor(density, noiseX, noiseY, noiseZ);

			// Apply slides
			density = this.applySlides(density, y);
			heightmapDensity = this.applySlides(heightmapDensity, y);

			primaryBuffer[y] = density;
			heightmapBuffer[y] = heightmapDensity;
		}
	}

	@Override
	protected PerlinOctaveNoise getForestOctaveNoise() {
		return this.forestOctaveNoise;
	}

	private double getOffset() {
		return 8D;
	}
}
