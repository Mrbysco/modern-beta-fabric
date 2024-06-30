package mod.bespectacled.modernbeta.world.carver;

import com.mojang.serialization.Codec;
import mod.bespectacled.modernbeta.util.BlockStates;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.WorldCarver;

import java.util.function.Function;

public class BetaCaveCarver extends WorldCarver<BetaCaveCarverConfig> {
	public BetaCaveCarver(Codec<BetaCaveCarverConfig> caveCodec) {
		super(caveCodec);
	}

	@Override
	public boolean isStartChunk(BetaCaveCarverConfig config, RandomSource random) {
		return true;
	}

	@Override
	public boolean carve(
			CarvingContext context,
			BetaCaveCarverConfig config,
			ChunkAccess mainChunk,
			Function<BlockPos, Holder<Biome>> posToBiome,
			RandomSource random,
			Aquifer aquiferSampler,
			ChunkPos pos,
			CarvingMask carvingMask
	) {
		boolean useFixedCaves = config.useFixedCaves.orElse(false);

		int caveCount = random.nextInt(random.nextInt(random.nextInt(40) + 1) + 1);
		if (random.nextInt(getMaxCaveCount()) != 0) {
			caveCount = 0;
		}

		for (int i = 0; i < caveCount; ++i) {
			double x = pos.getBlockX(random.nextInt(16)); // Starts
			double y = config.y.sample(random, context); // 1.17 stuff
			double z = pos.getBlockZ(random.nextInt(16));

			// 1.17 stuff
			double horizontalScale = config.horizontalRadiusMultiplier.sample(random);
			double verticalScale = config.verticalRadiusMultiplier.sample(random);
			double floorLevel = config.floorLevel.sample(random);

			WorldCarver.CarveSkipChecker skipPredicate = (carverContext, scaledRelativeX, scaledRelativeY, scaledRelativeZ, relativeY) ->
					!this.isPositionExcluded(scaledRelativeX, scaledRelativeY, scaledRelativeZ, floorLevel);

			int tunnelCount = 1;
			if (random.nextInt(4) == 0) {
				double yScale = config.yScale.sample(random);

				this.carveCave(
						context,
						config,
						mainChunk,
						random,
						mainChunk.getPos().x,
						mainChunk.getPos().z,
						x, y, z,
						yScale,
						skipPredicate,
						carvingMask,
						aquiferSampler,
						useFixedCaves
				);
				tunnelCount += random.nextInt(4);
			}

			for (int j = 0; j < tunnelCount; ++j) {
				float yaw = random.nextFloat() * 3.141593F * 2.0F;
				float pitch = ((random.nextFloat() - 0.5F) * 2.0F) / 8F;
				float width = getTunnelSystemWidth(random);

				this.carveTunnels(
						context,
						config,
						mainChunk,
						random,
						mainChunk.getPos().x, mainChunk.getPos().z,
						x, y, z,
						horizontalScale, verticalScale,
						width, yaw, pitch,
						0, 0, 1.0D,
						skipPredicate,
						carvingMask,
						aquiferSampler,
						useFixedCaves
				);
			}
		}

		return true;
	}

	private void carveCave(
			CarvingContext context,
			BetaCaveCarverConfig config,
			ChunkAccess chunk,
			RandomSource random,
			int mainChunkX,
			int mainChunkZ,
			double x,
			double y,
			double z,
			double yScale,
			WorldCarver.CarveSkipChecker skipPredicate,
			CarvingMask carvingMask,
			Aquifer aquiferSampler,
			boolean useFixedCaves
	) {
		this.carveTunnels(
				context,
				config,
				chunk,
				random,
				mainChunkX,
				mainChunkZ,
				x, y, z,
				1.0, 1.0,
				1.0F + random.nextFloat() * 6F,
				0.0F, 0.0F,
				-1, -1, yScale,
				skipPredicate,
				carvingMask,
				aquiferSampler,
				useFixedCaves
		);
	}

	private void carveTunnels(
			CarvingContext context,
			BetaCaveCarverConfig config,
			ChunkAccess chunk,
			RandomSource initialRandom,
			int mainChunkX,
			int mainChunkZ,
			double x,
			double y,
			double z,
			double horizontalScale,
			double verticalScale,
			float width,
			float yaw,
			float pitch,
			int branch,
			int branchCount,
			double yawPitchRatio,
			WorldCarver.CarveSkipChecker skipPredicate,
			CarvingMask carvingMask,
			Aquifer aquiferSampler,
			boolean useFixedCaves
	) {
		float f2 = 0.0F;
		float f3 = 0.0F;

		RandomSource random = new SingleThreadedRandomSource(initialRandom.nextLong());

		if (branchCount <= 0) {
			int someNumMaxStarts = 8 * 16 - 16;
			branchCount = someNumMaxStarts - random.nextInt(someNumMaxStarts / 4);
		}

		boolean noStarts = false;
		if (branch == -1) {
			branch = branchCount / 2;
			noStarts = true;
		}

		int randBranch = random.nextInt(branchCount / 2) + branchCount / 4;
		boolean vary = random.nextInt(6) == 0;

		for (; branch < branchCount; branch++) {
			double tunnelHorizontalScale = 1.5D + (double) (Mth.sin(((float) branch * 3.141593F) / (float) branchCount)
					* width * 1.0F);
			double tunnelVerticalScale = tunnelHorizontalScale * yawPitchRatio;

			float f4 = Mth.cos(pitch);
			float f5 = Mth.sin(pitch);

			x += Mth.cos(yaw) * f4;
			y += f5;
			z += Mth.sin(yaw) * f4;

			pitch *= vary ? 0.92F : 0.7F;

			pitch += f3 * 0.1F;
			yaw += f2 * 0.1F;

			f3 *= 0.9F;
			f2 *= 0.75F;

			f3 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
			f2 += (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4F;

			if (!noStarts && branch == randBranch && width > 1.0F) {
				carveTunnels(
						context,
						config,
						chunk,
						useFixedCaves ? random : initialRandom,
						mainChunkX, mainChunkZ,
						x, y, z,
						horizontalScale, verticalScale,
						random.nextFloat() * 0.5F + 0.5F,
						yaw - 1.570796F, pitch / 3F,
						branch, branchCount, 1.0D,
						skipPredicate,
						carvingMask,
						aquiferSampler,
						useFixedCaves
				);
				carveTunnels(
						context,
						config,
						chunk,
						useFixedCaves ? random : initialRandom,
						mainChunkX, mainChunkZ,
						x, y, z,
						horizontalScale, verticalScale,
						random.nextFloat() * 0.5F + 0.5F,
						yaw + 1.570796F, pitch / 3F,
						branch, branchCount, 1.0D,
						skipPredicate,
						carvingMask,
						aquiferSampler,
						useFixedCaves
				);
				return;
			}

			if (!noStarts && random.nextInt(4) == 0) {
				continue;
			}

			if (!canCarveBranch(mainChunkX, mainChunkZ, x, z, branch, branchCount, width)) {
				return;
			}

			this.carveRegion(
					context,
					config,
					chunk,
					mainChunkX, mainChunkZ,
					x, y, z,
					tunnelHorizontalScale * horizontalScale,
					tunnelVerticalScale * verticalScale,
					skipPredicate,
					carvingMask,
					aquiferSampler
			);

			if (noStarts) {
				break;
			}
		}

	}

	private boolean carveRegion(
			CarvingContext context,
			BetaCaveCarverConfig config,
			ChunkAccess chunk,
			int mainChunkX,
			int mainChunkZ,
			double x,
			double y,
			double z,
			double horizontalScale,
			double verticalScale,
			WorldCarver.CarveSkipChecker skipPredicate,
			CarvingMask carvingMask,
			Aquifer aquiferSampler
	) {
		double ctrX = mainChunkX * 16 + 8;
		double ctrZ = mainChunkZ * 16 + 8;

		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		if ( // Check for valid tunnel starts, I guess? Or to prevent overlap?
				x < ctrX - 16D - horizontalScale * 2D || z < ctrZ - 16D - horizontalScale * 2D || x > ctrX + 16D + horizontalScale * 2D
						|| z > ctrZ + 16D + horizontalScale * 2D) {
			return false;
		}

		int mainChunkStartX = mainChunkX * 16;
		int mainChunkStartZ = mainChunkZ * 16;

		// Get min and max extents of tunnel, relative to chunk coords.
		int minX = Mth.floor(x - horizontalScale) - mainChunkStartX - 1;
		int maxX = Mth.floor(x + horizontalScale) - mainChunkStartX + 1;

		int minY = Mth.floor(y - verticalScale) - 1;
		int maxY = Mth.floor(y + verticalScale) + 1;

		int minZ = Mth.floor(z - horizontalScale) - mainChunkStartZ - 1;
		int maxZ = Mth.floor(z + horizontalScale) - mainChunkStartZ + 1;

		if (minX < 0) {
			minX = 0;
		}
		if (maxX > 16) {
			maxX = 16;
		}

		if (minY < context.getMinGenY() + 1) {
			minY = context.getMinGenY() + 1;
		}
		if (maxY > context.getMinGenY() + context.getGenDepth() - 8) {
			maxY = context.getMinGenY() + context.getGenDepth() - 8;
		}

		if (minZ < 0) {
			minZ = 0;
		}
		if (maxZ > 16) {
			maxZ = 16;
		}

		if (this.isRegionUncarvable(context, config, chunk, mainChunkX, mainChunkZ, minX, maxX, minY, maxY, minZ, maxZ)) {
			return false;
		}

		for (int localX = minX; localX < maxX; localX++) {
			double scaledRelX = (((double) (localX + mainChunkX * 16) + 0.5D) - x) / horizontalScale;

			for (int localZ = minZ; localZ < maxZ; localZ++) {
				double scaledRelZ = (((double) (localZ + mainChunkZ * 16) + 0.5D) - z) / horizontalScale;
				boolean isGrassBlock = false;

				for (int localY = maxY; localY > minY; localY--) {
					double scaledRelY = (((double) (localY - 1) + 0.5D) - y) / verticalScale;

					if (skipPredicate.shouldSkip(context, scaledRelX, scaledRelY, scaledRelZ, localY) ||
							carvingMask.get(localX, localY, localZ))
						continue;

					carvingMask.set(localX, localY, localZ);
					pos.set(localX, localY, localZ);

					BlockState state = chunk.getBlockState(pos);

					if (state.is(Blocks.GRASS_BLOCK)) {
						isGrassBlock = true;
					}

					// Don't use canCarveBlock for accuracy, for now.
					if (state.is(config.replaceable)) {
						int offsetX = chunk.getPos().getBlockX(localX);
						int offsetZ = chunk.getPos().getBlockZ(localZ);
						BlockPos carverPos = new BlockPos(offsetX, localY, offsetZ);

						BlockState carverState = this.getBlockState(context, config, carverPos, aquiferSampler);

						if (carverState != null) {
							chunk.setBlockState(carverPos, carverState, false);

							if (aquiferSampler.shouldScheduleFluidUpdate() && !carverState.getFluidState().isEmpty()) {
								chunk.markPosForPostprocessing(carverPos);
							}

							// Replaces carved-out dirt with grass, if block that was removed was grass.
							if (isGrassBlock && chunk.getBlockState(carverPos.below()).getBlock() == Blocks.DIRT) {
								chunk.setBlockState(carverPos.below(), BlockStates.GRASS_BLOCK, false);
							}
						}
					}
				}
			}
		}

		return true;
	}

	private BlockState getBlockState(CarvingContext context, BetaCaveCarverConfig config, BlockPos pos, Aquifer aquiferSampler) {
		if (pos.getY() < config.lavaLevel.resolveY(context)) {
			return BlockStates.LAVA;
		}

		boolean useAquifers = config.useAquifers.orElse(false);

		if (!useAquifers) {
			return BlockStates.AIR;
		}

		// TODO: Produces too many flooded caves, re-visit this later.

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		BlockState state = aquiferSampler.computeSubstance(new DensityFunction.SinglePointContext(x, y, z), 0.0);

		if (state == null) {
			return isDebugEnabled(config) ? config.debugSettings.getBarrierState() : null;
		}

		return isDebugEnabled(config) ? getDebugState(config, state) : state;
	}

	private boolean canCarveBranch(
			int mainChunkX,
			int mainChunkZ,
			double x,
			double z,
			int branch,
			int branchCount,
			float baseWidth
	) {
		double ctrX = mainChunkX * 16 + 8;
		double ctrZ = mainChunkZ * 16 + 8;

		double d1 = x - ctrX;
		double d2 = z - ctrZ;
		double d3 = branchCount - branch;
		double d4 = baseWidth + 2.0F + 16F;

		if ((d1 * d1 + d2 * d2) - d3 * d3 > d4 * d4) {
			return false;
		}

		return true;
	}

	private boolean isRegionUncarvable(
			CarvingContext context,
			BetaCaveCarverConfig config,
			ChunkAccess chunk,
			int mainChunkX,
			int mainChunkZ,
			int relMinX,
			int relMaxX,
			int minY,
			int maxY,
			int relMinZ,
			int relMaxZ
	) {
		BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();

		boolean useAquifers = config.useAquifers.orElse(false);

		for (int relX = relMinX; relX < relMaxX; relX++) {
			for (int relZ = relMinZ; relZ < relMaxZ; relZ++) {
				for (int relY = maxY + 1; relY >= minY - 1; relY--) {
					if (relY < context.getMinGenY() || relY >= context.getMinGenY() + context.getGenDepth()) {
						continue;
					}

					int lavaLevel = config.lavaLevel.resolveY(context);
					Block block = chunk.getBlockState(blockPos.set(relX, relY, relZ)).getBlock();

					// Don't carve into water bodies, unless useAquifers enabled
					if (!useAquifers && block == Blocks.WATER) {
						return true;
					}

					// Don't carve into lava aquifers that spawn above lava level, unless useAquifers enabled
					if (!useAquifers && block == Blocks.LAVA && relY >= lavaLevel) {
						return true;
					}

					if (relY != minY - 1 && isOnBoundary(relMinX, relMaxX, relMinZ, relMaxZ, relX, relZ)) {
						relY = minY;
					}
				}

			}
		}

		return false;
	}

	private boolean isPositionExcluded(
			double scaledRelativeX,
			double scaledRelativeY,
			double scaledRelativeZ,
			double floorY
	) {
		return
				scaledRelativeY > floorY &&
						scaledRelativeX * scaledRelativeX +
								scaledRelativeY * scaledRelativeY +
								scaledRelativeZ * scaledRelativeZ < 1.0D;
	}

	private boolean isOnBoundary(int minX, int maxX, int minZ, int maxZ, int relX, int relZ) {
		return relX != minX && relX != maxX - 1 && relZ != minZ && relZ != maxZ - 1;
	}

	protected int getCaveY(CarvingContext context, RandomSource random) {
		return random.nextInt(random.nextInt(120) + 8);
	}

	protected int getMaxCaveCount() {
		return 15;
	}

	protected float getTunnelSystemWidth(RandomSource random) {
		float width = random.nextFloat() * 2.0f + random.nextFloat();
		return width;
	}

	private static BlockState getDebugState(CarverConfiguration config, BlockState state) {
		if (state.is(Blocks.AIR)) {
			return config.debugSettings.getAirState();
		}

		if (state.is(Blocks.WATER)) {
			BlockState waterState = config.debugSettings.getWaterState();
			if (waterState.hasProperty(BlockStateProperties.WATERLOGGED)) {
				return (BlockState) waterState.setValue(BlockStateProperties.WATERLOGGED, true);
			}
			return waterState;
		}

		if (state.is(Blocks.LAVA)) {
			return config.debugSettings.getLavaState();
		}

		return state;
	}

	private static boolean isDebugEnabled(CarverConfiguration config) {
		return config.debugSettings.isDebugMode();
	}
}
