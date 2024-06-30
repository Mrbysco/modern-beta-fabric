package mod.bespectacled.modernbeta.world.chunk.provider;

import mod.bespectacled.modernbeta.api.world.chunk.ChunkProviderFinite;
import mod.bespectacled.modernbeta.api.world.chunk.surface.SurfaceConfig;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoiseCombined;
import mod.bespectacled.modernbeta.world.blocksource.BlockSourceRules;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.chunk.provider.indev.IndevTheme;
import mod.bespectacled.modernbeta.world.chunk.provider.indev.IndevType;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SnowyDirtBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;

public class ChunkProviderIndev extends ChunkProviderFinite {
	private PerlinOctaveNoiseCombined minHeightOctaveNoise;
	private PerlinOctaveNoiseCombined maxHeightOctaveNoise;
	private PerlinOctaveNoise mainHeightOctaveNoise;

	private PerlinOctaveNoise islandOctaveNoise;

	private PerlinOctaveNoise dirtOctaveNoise;
	private PerlinOctaveNoise floatingOctaveNoise;

	private PerlinOctaveNoiseCombined erodeOctaveNoise0;
	private PerlinOctaveNoiseCombined erodeOctaveNoise1;

	private PerlinOctaveNoise sandOctaveNoise;
	private PerlinOctaveNoise gravelOctaveNoise;

	private final IndevTheme levelTheme;
	private final IndevType levelType;

	private final BlockState fluidBlock;
	private final BlockState topsoilBlock;

	private int layers;
	private int waterLevel;

	public ChunkProviderIndev(ModernBetaChunkGenerator chunkGenerator, long seed) {
		super(chunkGenerator, seed);

		this.levelTheme = IndevTheme.fromId(this.chunkSettings.indevLevelTheme);
		this.levelType = IndevType.fromId(this.chunkSettings.indevLevelType);

		this.fluidBlock = this.isFloating() ? BlockStates.AIR : (this.isHell() ? BlockStates.LAVA : this.defaultFluid);
		this.topsoilBlock = this.isHell() ? BlockStates.PODZOL : BlockStates.GRASS_BLOCK;

		this.waterLevel = this.levelHeight / 2;
		this.layers = this.isFloating() ? (this.levelHeight - 64) / 48 + 1 : 1;
	}

	@Override
	public int getSeaLevel() {
		return this.waterLevel;
	}

	@Override
	public Block getLevelFluidBlock() {
		return this.fluidBlock.getBlock();
	}

	public IndevType getLevelType() {
		return this.levelType;
	}

	public IndevTheme getLevelTheme() {
		return this.levelTheme;
	}

	public void generateIndevHouse(ServerLevel world, BlockPos spawnPos) {
		this.setPhase("Building");

		int spawnX = spawnPos.getX();
		int spawnY = spawnPos.getY() + 1;
		int spawnZ = spawnPos.getZ();
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		Block floorBlock = this.isHell() ? Blocks.MOSSY_COBBLESTONE : Blocks.STONE;
		Block wallBlock = this.isHell() ? Blocks.MOSSY_COBBLESTONE : Blocks.OAK_PLANKS;

		for (int x = spawnX - 3; x <= spawnX + 3; ++x) {
			for (int y = spawnY - 2; y <= spawnY + 2; ++y) {
				for (int z = spawnZ - 3; z <= spawnZ + 3; ++z) {
					Block block = (y < spawnY - 1) ? Blocks.OBSIDIAN : Blocks.AIR;

					if (x == spawnX - 3 || z == spawnZ - 3 || x == spawnX + 3 || z == spawnZ + 3 || y == spawnY - 2 || y == spawnY + 2) {
						block = floorBlock;
						if (y >= spawnY - 1) {
							block = wallBlock;
						}
					}
					if (z == spawnZ + 3 && x == spawnX && y >= spawnY - 1 && y <= spawnY) {
						block = Blocks.AIR;
					}

					world.setBlockAndUpdate(pos.set(x, y, z), block.defaultBlockState());
				}
			}
		}

		world.setBlockAndUpdate(pos.set(spawnX - 3 + 1, spawnY, spawnZ), Blocks.WALL_TORCH.defaultBlockState().rotate(Rotation.CLOCKWISE_90));
		world.setBlockAndUpdate(pos.set(spawnX + 3 - 1, spawnY, spawnZ), Blocks.WALL_TORCH.defaultBlockState().rotate(Rotation.COUNTERCLOCKWISE_90));
	}

	@Override
	protected void pregenerateTerrain() {
		for (int layer = 0; layer < this.layers; ++layer) {
			// Floating island layer generation depends on water level being lowered on each iteration
			this.waterLevel = (this.levelType == IndevType.FLOATING) ? this.levelHeight - 32 - layer * 48 : this.waterLevel;

			// Noise Generators (Here instead of constructor to randomize floating layer generation)
			minHeightOctaveNoise = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(random, 8, false), new PerlinOctaveNoise(random, 8, false));
			maxHeightOctaveNoise = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(random, 8, false), new PerlinOctaveNoise(random, 8, false));

			mainHeightOctaveNoise = new PerlinOctaveNoise(random, 6, false);
			islandOctaveNoise = new PerlinOctaveNoise(random, 2, false);

			dirtOctaveNoise = new PerlinOctaveNoise(random, 8, false);
			floatingOctaveNoise = new PerlinOctaveNoise(random, 8, false);

			erodeOctaveNoise0 = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(random, 8, false), new PerlinOctaveNoise(random, 8, false));
			erodeOctaveNoise1 = new PerlinOctaveNoiseCombined(new PerlinOctaveNoise(random, 8, false), new PerlinOctaveNoise(random, 8, false));

			sandOctaveNoise = new PerlinOctaveNoise(random, 8, false);
			gravelOctaveNoise = new PerlinOctaveNoise(random, 8, false);

			this.generateHeightmap();
			this.erodeTerrain();
			this.soilTerrain();
			this.growSurface();
		}

		if (this.chunkSettings.indevUseCaves) this.carveTerrain();
		this.floodFluid();
		this.floodLava();
		this.plantSurface();
	}

	@Override
	protected void generateBorder(ChunkAccess chunk) {
		switch (this.levelType) {
			case ISLAND -> this.generateWaterBorder(chunk);
			case INLAND -> this.generateWorldBorder(chunk);
			case FLOATING -> {
			}
			default -> {
			}
		}
	}

	@Override
	protected BlockState postProcessTerrainState(Block block, BlockSourceRules blockSources, TerrainState terrainState, BlockPos pos, int topY) {
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		BlockState blockState = block.defaultBlockState();
		BlockState modifiedBlockState = blockSources.apply(x, y, z);

		boolean inFluid = modifiedBlockState.isAir() || modifiedBlockState.is(this.getLevelFluidBlock());
		int runDepth = terrainState.getRunDepth();

		// Check to see if structure weight sampler modifies terrain.
		if (!blockState.equals(modifiedBlockState)) {
			terrainState.terrainModified();
		}

		// Replace default block set by structure sampling with topsoil blocks.
		if (terrainState.isTerrainModified() && !inFluid) {
			if (runDepth == 0) {
				modifiedBlockState = (this.isFloating() || y >= this.waterLevel - 1) ?
						this.topsoilBlock :
						BlockStates.DIRT;
			}

			if (runDepth == 1) {
				modifiedBlockState = BlockStates.DIRT;
			}

			terrainState.incrementRunDepth();
		}

		return modifiedBlockState;
	}

	@Override
	protected void generateBedrock(ChunkAccess chunk, Block block, BlockPos pos) {
		int y = pos.getY();

		if (this.isFloating())
			return;

		if (y <= 1 + this.bedrockFloor && block == Blocks.AIR) {
			chunk.setBlockState(pos, BlockStates.BEDROCK, false);
		} else if (y <= 1 + this.bedrockFloor) {
			chunk.setBlockState(pos, BlockStates.BEDROCK, false);
		}
	}

	@Override
	protected BlockState postProcessSurfaceState(BlockState blockState, SurfaceConfig surfaceConfig, BlockPos pos, boolean isCold) {
		BlockState topBlock = surfaceConfig.normal().topBlock();
		BlockState fillerBlock = surfaceConfig.normal().fillerBlock();

		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		// Skip replacing surface blocks if this is a Hell level and biome surface is standard grass/dirt.
		if (this.isHell() && topBlock.equals(BlockStates.GRASS_BLOCK) && fillerBlock.equals(BlockStates.DIRT))
			return blockState;

		if (blockState.is(this.topsoilBlock.getBlock())) {
			blockState = topBlock;
		} else if (blockState.is(BlockStates.DIRT.getBlock())) {
			blockState = fillerBlock;
		}

		// Set snow/ice
		if (!this.inWorldBounds(x, z)) {
			if (y == this.seaLevel) {
				if (isCold && blockState.equals(topBlock)) {
					blockState = topBlock.setValue(SnowyDirtBlock.SNOWY, true);
				}

			} else if (y == this.seaLevel - 1 && this.levelTheme != IndevTheme.HELL) {
				if (isCold && blockState.equals(BlockStates.WATER)) {
					blockState = BlockStates.ICE;
				}
			}
		}

		return blockState;
	}

	private void generateHeightmap() {
		this.setPhase("Raising");

		for (int x = 0; x < this.levelWidth; ++x) {
			double normalizedX = Math.abs((x / (this.levelWidth - 1.0) - 0.5) * 2.0);

			for (int z = 0; z < this.levelLength; ++z) {
				double normalizedZ = Math.abs((z / (this.levelLength - 1.0) - 0.5) * 2.0);

				double heightLow = minHeightOctaveNoise.sample(x * 1.3f, z * 1.3f) / 6.0 - 4.0;
				double heightHigh = maxHeightOctaveNoise.sample(x * 1.3f, z * 1.3f) / 5.0 + 10.0 - 4.0;

				double heightSelector = mainHeightOctaveNoise.sampleXY(x, z) / 8.0;

				if (heightSelector > 0.0) {
					heightHigh = heightLow;
				}

				double heightResult = Math.max(heightLow, heightHigh) / 2.0;

				if (this.levelType == IndevType.ISLAND) {
					double islandRadius = Math.sqrt(normalizedX * normalizedX + normalizedZ * normalizedZ) * 1.2;
					islandRadius = Math.min(islandRadius, islandOctaveNoise.sampleXY(x * 0.05f, z * 0.05f) / 4.0 + 1.0);
					islandRadius = Math.max(islandRadius, Math.max(normalizedX, normalizedZ));

					if (islandRadius > 1.0) {
						islandRadius = 1.0;
					} else if (islandRadius < 0.0) {
						islandRadius = 0.0;
					}

					islandRadius *= islandRadius;
					heightResult = heightResult * (1.0 - islandRadius) - islandRadius * 10.0 + 5.0;

					if (heightResult < 0.0) {
						heightResult -= heightResult * heightResult * 0.20000000298023224;
					}


				} else if (heightResult < 0.0) {
					heightResult *= 0.8;
				}

				this.heightmap[x + z * this.levelWidth] = (int) heightResult;
			}
		}
	}

	private void erodeTerrain() {
		this.setPhase("Eroding");

		for (int x = 0; x < this.levelWidth; ++x) {
			for (int z = 0; z < this.levelLength; ++z) {
				double erodeSelector = erodeOctaveNoise0.sample(x << 1, z << 1) / 8.0;
				int erodeNoise = erodeOctaveNoise1.sample(x << 1, z << 1) > 0.0 ? 1 : 0;

				if (erodeSelector > 2.0) {
					int heightResult = this.heightmap[x + z * this.levelWidth];
					heightResult = ((heightResult - erodeNoise) / 2 << 1) + erodeNoise;

					this.heightmap[x + z * this.levelWidth] = heightResult;
				}
			}
		}
	}

	private void soilTerrain() {
		this.setPhase("Soiling");
		int seaLevel = this.getSeaLevel();

		for (int x = 0; x < this.levelWidth; ++x) {
			double normalizedX = Math.abs((x / (this.levelWidth - 1.0) - 0.5) * 2.0);

			for (int z = 0; z < this.levelLength; ++z) {
				double normalizedZ = Math.max(normalizedX, Math.abs(z / (this.levelLength - 1.0) - 0.5) * 2.0);
				normalizedZ = normalizedZ * normalizedZ * normalizedZ;

				int dirtThickness = (int) (dirtOctaveNoise.sampleXY(x, z) / 24.0) - 4;
				int dirtThreshold = this.heightmap[x + z * this.levelWidth] + seaLevel;

				int stoneThreshold = dirtThickness + dirtThreshold;
				this.heightmap[x + z * this.levelWidth] = Math.max(dirtThreshold, stoneThreshold);

				if (this.heightmap[x + z * this.levelWidth] > this.levelHeight - 2) {
					this.heightmap[x + z * this.levelWidth] = this.levelHeight - 2;
				}

				if (this.heightmap[x + z * this.levelWidth] <= 0) {
					this.heightmap[x + z * this.levelWidth] = 1;
				}

				double floatingNoise = floatingOctaveNoise.sampleXY(x * 2.3, z * 2.3) / 24.0;

				// Rounds out the bottom of terrain to form floating islands
				int roundedHeight = (int) (Math.sqrt(Math.abs(floatingNoise)) * Math.signum(floatingNoise) * 20.0) + seaLevel;
				roundedHeight = (int) (roundedHeight * (1.0 - normalizedZ) + normalizedZ * this.levelHeight);

				if (roundedHeight > seaLevel) {
					roundedHeight = this.levelHeight;
				}

				for (int y = 0; y < this.levelHeight; ++y) {
					Block block = Blocks.AIR;

					if (y <= dirtThreshold)
						block = Blocks.DIRT;

					if (y <= stoneThreshold)
						block = Blocks.STONE;

					if (this.levelType == IndevType.FLOATING && y < roundedHeight)
						block = Blocks.AIR;

					Block existingBlock = this.getLevelBlock(x, y, z);

					if (existingBlock.equals(Blocks.AIR)) {
						this.setLevelBlock(x, y, z, block);
					}
				}
			}
		}
	}

	private void growSurface() {
		this.setPhase("Growing");
		int surfaceLevel = this.getSeaLevel() - 1;

		if (this.levelTheme == IndevTheme.PARADISE) surfaceLevel += 2;

		for (int x = 0; x < this.levelWidth; ++x) {
			for (int z = 0; z < this.levelLength; ++z) {
				boolean genSand = sandOctaveNoise.sampleXY(x, z) > 8.0;
				boolean genGravel = gravelOctaveNoise.sampleXY(x, z) > 12.0;

				if (this.levelType == IndevType.ISLAND) {
					genSand = sandOctaveNoise.sampleXY(x, z) > -8.0;
				}

				if (this.levelTheme == IndevTheme.PARADISE) {
					genSand = sandOctaveNoise.sampleXY(x, z) > -32.0;
				}

				if (this.levelTheme == IndevTheme.HELL || this.levelTheme == IndevTheme.WOODS) {
					genSand = sandOctaveNoise.sampleXY(x, z) > -8.0;
				}

				int heightResult = heightmap[x + z * this.levelWidth];
				Block block = this.getLevelBlock(x, heightResult, z);
				Block blockUp = this.getLevelBlock(x, heightResult + 1, z);

				// TODO: this.getSeaLevel() - 1 might be wrong, might be surfaceLevel instead
				if ((blockUp == this.fluidBlock.getBlock() || blockUp == Blocks.AIR) && heightResult <= this.getSeaLevel() - 1 && genGravel) {
					this.setLevelBlock(x, heightResult, z, Blocks.GRAVEL);
				}

				if (blockUp == Blocks.AIR) {
					Block surfaceBlock = null;

					if (heightResult <= surfaceLevel && genSand) {
						surfaceBlock = (this.levelTheme == IndevTheme.HELL) ? Blocks.GRASS_BLOCK : Blocks.SAND;
					}

					if (block != Blocks.AIR && surfaceBlock != null) {
						this.setLevelBlock(x, heightResult, z, surfaceBlock);
					}
				}
			}
		}
	}

	private void carveTerrain() {
		this.setPhase("Carving");

		int caveCount = this.levelWidth * this.levelLength * this.levelHeight / 256 / 64 << 1;

		for (int i = 0; i < caveCount; ++i) {
			float caveX = random.nextFloat() * this.levelWidth;
			float caveY = random.nextFloat() * this.levelHeight;
			float caveZ = random.nextFloat() * this.levelLength;

			int caveLen = (int) ((random.nextFloat() + random.nextFloat()) * 200F);

			float theta = random.nextFloat() * 3.1415927f * 2.0f;
			float deltaTheta = 0.0f;
			float phi = random.nextFloat() * 3.1415927f * 2.0f;
			float deltaPhi = 0.0f;

			float caveRadius = random.nextFloat() * random.nextFloat() * this.caveRadius;

			for (int len = 0; len < caveLen; ++len) {
				caveX += Mth.sin(theta) * Mth.cos(phi);
				caveZ += Mth.cos(theta) * Mth.cos(phi);
				caveY += Mth.sin(phi);

				theta = theta + deltaTheta * 0.2f;
				deltaTheta = (deltaTheta * 0.9f) + (random.nextFloat() - random.nextFloat());
				phi = phi * 0.5f + deltaPhi * 0.25f;
				deltaPhi = (deltaPhi * 0.75f) + (random.nextFloat() - random.nextFloat());

				if (random.nextFloat() >= 0.25f) {
					float centerX = caveX + (random.nextFloat() * 4.0f - 2.0f) * 0.2f;
					float centerY = caveY + (random.nextFloat() * 4.0f - 2.0f) * 0.2f;
					float centerZ = caveZ + (random.nextFloat() * 4.0f - 2.0f) * 0.2f;

					float radius = (this.levelHeight - centerY) / this.levelHeight;
					radius = 1.2f + (radius * 3.5f + 1.0f) * caveRadius;
					radius = radius * Mth.sin(len * 3.1415927f / caveLen);

					fillOblateSpheroid(centerX, centerY, centerZ, radius, Blocks.AIR);
				}
			}
		}
	}

	// Using Classic generation algorithm
	private void floodFluid() {
		this.setPhase("Watering");

		Block fluid = this.fluidBlock.getBlock();

		if (this.levelType == IndevType.FLOATING) {
			return;
		}

		for (int x = 0; x < this.levelWidth; ++x) {
			flood(x, this.waterLevel - 1, 0, fluid);
			flood(x, this.waterLevel - 1, this.levelLength - 1, fluid);
		}

		for (int z = 0; z < this.levelLength; ++z) {
			flood(this.levelWidth - 1, this.waterLevel - 1, z, fluid);
			flood(0, this.waterLevel - 1, z, fluid);
		}

		int waterSourceCount = this.levelWidth * this.levelLength / 8000;

		for (int i = 0; i < waterSourceCount; ++i) {
			int randX = random.nextInt(this.levelWidth);
			int randZ = random.nextInt(this.levelLength);
			int randY = (this.waterLevel - 1) - random.nextInt(2);

			this.flood(randX, randY, randZ, fluid);
		}

	}

	// Using Classic generation algorithm
	private void floodLava() {
		this.setPhase("Melting");

		if (this.levelType == IndevType.FLOATING) {
			return;
		}

		int lavaSourceCount = this.levelWidth * this.levelLength / 20000;

		for (int i = 0; i < lavaSourceCount; ++i) {
			int randX = random.nextInt(this.levelWidth);
			int randZ = random.nextInt(this.levelLength);
			int randY = (int) ((float) (this.waterLevel - 3) * random.nextFloat() * random.nextFloat());

			this.flood(randX, randY, randZ, Blocks.LAVA);
		}

	}

	private void plantSurface() {
		this.setPhase("Planting");

		Block blockToPlant = this.topsoilBlock.getBlock();

		for (int x = 0; x < this.levelWidth; ++x) {
			for (int z = 0; z < this.levelLength; ++z) {
				for (int y = 0; y < this.levelHeight - 2; ++y) {
					Block block = this.getLevelBlock(x, y, z);
					Block blockUp = this.getLevelBlock(x, y + 1, z);

					if (block.equals(Blocks.DIRT) && blockUp.equals(Blocks.AIR)) {
						this.setLevelBlock(x, y, z, blockToPlant);
					}
				}
			}
		}
	}

	private void generateWorldBorder(ChunkAccess chunk) {
		BlockState topBlock = this.topsoilBlock;
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int x = 0; x < 16; ++x) {
			for (int z = 0; z < 16; ++z) {
				for (int y = 0; y < this.levelHeight; ++y) {
					pos.set(x, y, z);

					if (y < this.waterLevel) {
						chunk.setBlockState(pos, BlockStates.BEDROCK, false);
					} else if (y == this.waterLevel) {
						chunk.setBlockState(pos, topBlock, false);
					}
				}
			}
		}
	}

	private void generateWaterBorder(ChunkAccess chunk) {
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

		for (int x = 0; x < 16; ++x) {
			for (int z = 0; z < 16; ++z) {
				for (int y = 0; y < this.levelHeight; ++y) {
					pos.set(x, y, z);

					if (y < this.waterLevel - 10) {
						chunk.setBlockState(pos, BlockStates.BEDROCK, false);
					} else if (y == this.waterLevel - 10) {
						chunk.setBlockState(pos, BlockStates.DIRT, false);
					} else if (y < this.waterLevel) {
						chunk.setBlockState(pos, this.fluidBlock, false);
					}
				}
			}
		}
	}

	private boolean isHell() {
		return this.levelTheme == IndevTheme.HELL;
	}

	private boolean isFloating() {
		return this.levelType == IndevType.FLOATING;
	}

}
