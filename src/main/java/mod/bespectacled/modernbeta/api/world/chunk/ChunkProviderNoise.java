package mod.bespectacled.modernbeta.api.world.chunk;

import com.google.common.collect.Sets;
import mod.bespectacled.modernbeta.api.world.blocksource.BlockSource;
import mod.bespectacled.modernbeta.api.world.chunk.noise.NoisePostProcessor;
import mod.bespectacled.modernbeta.api.world.chunk.noise.NoiseProvider;
import mod.bespectacled.modernbeta.api.world.chunk.noise.NoiseProviderBase;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.util.chunk.ChunkCache;
import mod.bespectacled.modernbeta.util.chunk.ChunkHeightmap;
import mod.bespectacled.modernbeta.util.noise.SimpleNoisePos;
import mod.bespectacled.modernbeta.util.noise.SimplexNoise;
import mod.bespectacled.modernbeta.world.blocksource.BlockSourceRules;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkNoiseSampler;
import mod.bespectacled.modernbeta.world.chunk.provider.island.IslandShape;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.NoiseSettings;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public abstract class ChunkProviderNoise extends ChunkProvider {
	protected final int worldMinY;
	protected final int worldHeight;
	protected final int worldTopY;
	protected final int seaLevel;

	protected final int bedrockFloor;
	protected final int bedrockCeiling;

	protected final boolean useDeepslate;

	protected final BlockState defaultBlock;
	protected final BlockState defaultFluid;

	protected final int noiseResolutionVertical;   // Number of blocks in a vertical subchunk
	protected final int noiseResolutionHorizontal; // Number of blocks in a horizontal subchunk

	protected final int noiseSizeX; // Number of horizontal subchunks along x
	protected final int noiseSizeZ; // Number of horizontal subchunks along z
	protected final int noiseSizeY; // Number of vertical subchunks
	protected final int noiseMinY;  // Subchunk index of bottom of the world
	protected final int noiseTopY;  // Number of positive (y >= 0) vertical subchunks

	private final ChunkCache<NoiseProviderBase> chunkCacheNoise;
	private final ChunkCache<ChunkHeightmap> chunkCacheHeightmap;

	private final NoisePostProcessor noisePostProcessor;
	private final SimplexNoise islandNoise;

	public ChunkProviderNoise(ModernBetaChunkGenerator chunkGenerator, long seed) {
		super(chunkGenerator, seed);

		NoiseGeneratorSettings generatorSettings = chunkGenerator.getGeneratorSettings().value();
		NoiseSettings shapeConfig = generatorSettings.noiseSettings();

		this.worldMinY = shapeConfig.minY();
		this.worldHeight = shapeConfig.height();
		this.worldTopY = this.worldHeight + this.worldMinY;
		this.seaLevel = generatorSettings.seaLevel();

		this.bedrockFloor = this.worldMinY;
		this.bedrockCeiling = this.worldTopY;

		this.useDeepslate = this.chunkSettings.useDeepslate;

		this.defaultBlock = generatorSettings.defaultBlock();
		this.defaultFluid = generatorSettings.defaultFluid();

		this.noiseResolutionVertical = shapeConfig.noiseSizeVertical() * 4;
		this.noiseResolutionHorizontal = shapeConfig.noiseSizeHorizontal() * 4;

		this.noiseSizeX = 16 / this.noiseResolutionHorizontal;
		this.noiseSizeZ = 16 / this.noiseResolutionHorizontal;
		this.noiseSizeY = Mth.floorDiv(this.worldHeight, this.noiseResolutionVertical);
		this.noiseMinY = Mth.floorDiv(this.worldMinY, this.noiseResolutionVertical);
		this.noiseTopY = Mth.floorDiv(this.worldMinY + this.worldHeight, this.noiseResolutionVertical);

		this.chunkCacheNoise = new ChunkCache<>(
				"base_noise",
				(chunkX, chunkZ) -> {
					NoiseProviderBase noiseProviderBase = new NoiseProviderBase(
							this.noiseSizeX,
							this.noiseSizeY,
							this.noiseSizeZ,
							this::sampleNoiseColumn
					);

					noiseProviderBase.sampleInitialNoise(chunkX * this.noiseSizeX, chunkZ * this.noiseSizeZ);

					return noiseProviderBase;
				}
		);
		this.chunkCacheHeightmap = new ChunkCache<>("heightmap", this::sampleHeightmap);

		this.noisePostProcessor = NoisePostProcessor.DEFAULT;
		this.islandNoise = new SimplexNoise(new Random(this.seed));
	}

	/**
	 * Generates base terrain for given chunk and returns it.
	 *
	 * @param blender
	 * @param structureAccessor
	 * @param chunk
	 * @param noiseConfig
	 * @return A completed chunk.
	 */
	@Override
	public CompletableFuture<ChunkAccess> provideChunk(Blender blender, StructureManager structureAccessor, ChunkAccess chunk, RandomState noiseConfig) {
		NoiseSettings shapeConfig = this.generatorSettings.value().noiseSettings();

		int minY = Math.max(shapeConfig.minY(), chunk.getMinBuildHeight());
		int topY = Math.min(shapeConfig.minY() + shapeConfig.height(), chunk.getMaxBuildHeight());

		@SuppressWarnings("unused")
		int noiseMinY = Mth.floorDiv(minY, this.noiseResolutionVertical);
		int noiseTopY = Mth.floorDiv(topY - minY, this.noiseResolutionVertical);

		if (noiseTopY <= 0) {
			return CompletableFuture.completedFuture(chunk);
		}

		int sectionTopY = chunk.getSectionIndex(noiseTopY * this.noiseResolutionVertical - 1 + minY);
		int sectionMinY = chunk.getSectionIndex(minY);

		HashSet<LevelChunkSection> sections = Sets.newHashSet();
		for (int sectionNdx = sectionTopY; sectionNdx >= sectionMinY; --sectionNdx) {
			LevelChunkSection section = chunk.getSection(sectionNdx);

			section.acquire();
			sections.add(section);
		}

		this.generateTerrain(chunk, structureAccessor, noiseConfig);

		return CompletableFuture.supplyAsync(() -> chunk, Util.backgroundExecutor())
				.whenCompleteAsync((arg, throwable) -> {
					for (LevelChunkSection section : sections) {
						section.release();
					}
				});
	}

	/**
	 * Sample height at given x/z coordinate. Initially generates heightmap for entire chunk,
	 * if chunk containing x/z coordinates has never been sampled.
	 *
	 * @param x    x-coordinate in block coordinates.
	 * @param z    z-coordinate in block coordinates.
	 * @param type Vanilla heightmap type.
	 * @return The y-coordinate of top block at x/z.
	 */
	@Override
	public int getHeight(int x, int z, Heightmap.Types type) {
		int chunkX = x >> 4;
		int chunkZ = z >> 4;

		return this.chunkCacheHeightmap.get(chunkX, chunkZ).getHeight(x, z, type);
	}

	/**
	 * Sample height at given x/z coordinate. Initially generates heightmap for entire chunk,
	 * if chunk containing x/z coordinates has never been sampled.
	 *
	 * @param x    x-coordinate in block coordinates.
	 * @param z    z-coordinate in block coordinates.
	 * @param type HeightmapChunk heightmap type.
	 * @return The y-coordinate of top block at x/z.
	 */
	public int getHeight(int x, int z, ChunkHeightmap.Type type) {
		int chunkX = x >> 4;
		int chunkZ = z >> 4;

		return this.chunkCacheHeightmap.get(chunkX, chunkZ).getHeight(x, z, type);
	}

	/**
	 * Create a new aquifer sampler.
	 *
	 * @param chunk
	 * @param noiseConfig
	 * @return A new aquifer sampler.
	 */
	@Override
	public Aquifer getAquiferSampler(ChunkAccess chunk, RandomState noiseConfig) {
		PositionalRandomFactory randomDeriver = this.randomProvider.newInstance(this.seed).forkPositional();
		ModernBetaChunkNoiseSampler noiseSampler = ModernBetaChunkNoiseSampler.create(
				chunk,
				noiseConfig,
				this.generatorSettings.value(),
				this.getFluidLevelSampler(),
				this
		);

		AquiferSamplerProvider aquiferSamplerProvider = new AquiferSamplerProvider(
				this.generatorSettings.value().noiseRouter(),
				randomDeriver,
				noiseSampler,
				this.defaultFluid,
				this.seaLevel,
				this.worldMinY + 10,
				this.worldMinY,
				this.worldHeight,
				this.noiseResolutionVertical,
				this.generatorSettings.value().aquifersEnabled()
		);

		return aquiferSamplerProvider.provideAquiferSampler(chunk);
	}

	/**
	 * Generates noise for a column at startNoiseX + localNoiseX / startNoiseZ + localNoiseZ.
	 *
	 * @param primaryBuffer   Primary heightmap buffer, with noise caves.
	 * @param heightmapBuffer Heightmap buffer, identical to primaryBuffer sans noise caves.
	 * @param startNoiseX     x-coordinate start of chunk in noise coordinates.
	 * @param startNoiseZ     z-coordinate start of chunk in noise coordinates.
	 * @param localNoiseX     Current subchunk index along x-axis.
	 * @param localNoiseZ     Current subchunk index along z-axis.
	 */
	protected abstract void sampleNoiseColumn(
			double[] primaryBuffer,
			double[] heightmapBuffer,
			int startNoiseX,
			int startNoiseZ,
			int localNoiseX,
			int localNoiseZ
	);

	/**
	 * Check if default noise post processor (i.e. NONE) is being used.
	 *
	 * @return Whether default noise post processor is being used.
	 */
	protected boolean hasNoisePostProcessor() {
		return this.noisePostProcessor != NoisePostProcessor.DEFAULT;
	}

	/**
	 * Samples density for noise post processor.
	 *
	 * @param noise             Base density.
	 * @param noiseX            x-coordinate in absolute noise coordinates.
	 * @param noiseY            y-coordinate in absolute noise coordinates.
	 * @param noiseZ            z-coordinate in absolute noise coordinates.
	 * @param generatorSettings Vanilla chunk generator settings.
	 * @param chunkSettings     Modern Beta chunk generator settings.
	 * @return Modified noise density.
	 */
	protected double sampleNoisePostProcessor(double noise, int noiseX, int noiseY, int noiseZ) {
		return this.noisePostProcessor.sample(noise, noiseX, noiseY, noiseZ, this.generatorSettings.value(), this.chunkSettings);
	}

	/**
	 * Calculate a noise offset for generating islands.
	 *
	 * @param noiseX
	 * @param noiseZ
	 * @return A noise addition.
	 */
	protected double getIslandOffset(int noiseX, int noiseZ) {
		if (!this.chunkSettings.islesUseIslands) {
			return 0.0;
		}

		Function<Integer, Integer> toNoiseCoord = chunkCoord -> chunkCoord * this.noiseSizeX;
		IslandShape islandShape = IslandShape.fromId(this.chunkSettings.islesCenterIslandShape);

		double distance = islandShape.getDistance(noiseX, noiseZ);
		double oceanSlideTarget = this.chunkSettings.islesOceanSlideTarget;

		int centerIslandRadius = toNoiseCoord.apply(this.chunkSettings.islesCenterIslandRadius);
		int centerIslandFalloffDistance = toNoiseCoord.apply(this.chunkSettings.islesCenterIslandFalloffDistance);

		int centerOceanRadius = toNoiseCoord.apply(this.chunkSettings.islesCenterOceanRadius);
		int centerOceanFalloffDistance = toNoiseCoord.apply(this.chunkSettings.islesCenterOceanFalloffDistance);

		double outerIslandNoiseScale = this.chunkSettings.islesOuterIslandNoiseScale;
		double outerIslandNoiseOffset = this.chunkSettings.islesOuterIslandNoiseOffset;

		double islandDelta = (distance - centerIslandRadius) / centerIslandFalloffDistance;
		double islandOffset = Mth.clampedLerp(0.0, oceanSlideTarget, islandDelta);

		if (this.chunkSettings.islesUseOuterIslands && distance > centerOceanRadius) {
			double islandAddition = (float) this.islandNoise.sample(
					noiseX / outerIslandNoiseScale,
					noiseZ / outerIslandNoiseScale,
					1.0,
					1.0
			) + outerIslandNoiseOffset;

			// 0.885539 = Simplex upper range, but scale a little higher to ensure island centers have untouched terrain.
			islandAddition /= 0.8F;
			islandAddition = Mth.clamp(islandAddition, 0.0F, 1.0F);

			// Interpolate noise addition so there isn't a sharp cutoff at start of ocean ring edge.
			double oceanDelta = (distance - centerOceanRadius) / centerOceanFalloffDistance;
			islandAddition = (double) Mth.clampedLerp(0.0F, islandAddition, oceanDelta);

			islandOffset += islandAddition * -oceanSlideTarget;
			islandOffset = Mth.clamp(islandOffset, oceanSlideTarget, 0.0F);
		}

		return islandOffset;
	}

	/**
	 * Interpolate density to set terrain curve at top and bottom of world.
	 *
	 * @param density Base density.
	 * @param noiseY  y-coordinate in noise coordinates from [0, noiseSizeY]
	 * @return Modified noise density.
	 */
	protected double applySlides(double density, int noiseY) {
		if (this.chunkSettings.noiseTopSlideSize > 0.0) {
			double delta = ((double) (this.noiseSizeY - noiseY) - this.chunkSettings.noiseTopSlideOffset) / this.chunkSettings.noiseTopSlideSize;
			density = Mth.clampedLerp(this.chunkSettings.noiseTopSlideTarget, density, delta);
		}

		if (this.chunkSettings.noiseBottomSlideSize > 0.0) {
			double delta = ((double) noiseY - this.chunkSettings.noiseBottomSlideOffset) / this.chunkSettings.noiseBottomSlideSize;
			density = Mth.clampedLerp(this.chunkSettings.noiseBottomSlideTarget, density, delta);
		}

		return density;
	}

	/**
	 * Schedules fluid tick for aquifer sampler, so water flows when generated.
	 *
	 * @param chunk
	 * @param aquiferSampler
	 * @param pos            BlockPos in block coordinates.
	 * @param blockState     Blockstate at pos.
	 */
	protected void scheduleFluidTick(ChunkAccess chunk, Aquifer aquiferSampler, BlockPos pos, BlockState blockState) {
		if (aquiferSampler.shouldScheduleFluidUpdate() && !blockState.getFluidState().isEmpty()) {
			chunk.markPosForPostprocessing(pos);
		}
	}

	/**
	 * Gets heightmap for given set of chunk coordinates.
	 *
	 * @param chunkX
	 * @param chunkZ
	 * @return Heightmap for chunk.
	 */
	protected ChunkHeightmap getChunkHeightmap(int chunkX, int chunkZ) {
		return this.chunkCacheHeightmap.get(chunkX, chunkZ);
	}

	/**
	 * Generates the base terrain for a given chunk.
	 *
	 * @param chunk
	 * @param structureAccessor Collects structures within the chunk, so that terrain can be modified to accommodate them.
	 * @param noiseConfig       NoiseConfig
	 */
	private void generateTerrain(ChunkAccess chunk, StructureManager structureAccessor, RandomState noiseConfig) {
		ChunkPos chunkPos = chunk.getPos();
		int chunkX = chunkPos.x;
		int chunkZ = chunkPos.z;
		int startX = chunkPos.getMinBlockX();
		int startZ = chunkPos.getMinBlockZ();

		Heightmap heightmapOcean = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.OCEAN_FLOOR_WG);
		Heightmap heightmapSurface = chunk.getOrCreateHeightmapUnprimed(Heightmap.Types.WORLD_SURFACE_WG);

		Beardifier structureWeightSampler = Beardifier.forStructuresInChunk(structureAccessor, chunkPos);
		Aquifer aquiferSampler = this.getAquiferSampler(chunk, noiseConfig);
		BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

		// Create and populate noise providers
		List<NoiseProvider> noiseProviders = new ArrayList<>();

		NoiseProvider baseNoiseProvider = this.chunkCacheNoise.get(chunkX, chunkZ);
		BlockSource baseBlockSource = this.getBaseBlockSource(
				baseNoiseProvider,
				structureWeightSampler,
				aquiferSampler,
				new SimpleNoisePos()
		);

		// Create and populate block sources
		BlockSourceRules.Builder builder = new BlockSourceRules.Builder().add(baseBlockSource);
		this.blockSources.forEach(blockSource -> builder.add(blockSource));

		BlockSourceRules blockSources = builder.build(this.defaultBlock);

		// Sample initial noise.
		// Base noise should be added after this,
		// since base noise is sampled when fetched from cache.
		noiseProviders.forEach(noiseProvider -> noiseProvider.sampleInitialNoise(chunkX * this.noiseSizeX, chunkZ * this.noiseSizeZ));
		noiseProviders.add(baseNoiseProvider);

		for (int subChunkX = 0; subChunkX < this.noiseSizeX; ++subChunkX) {
			int noiseX = subChunkX;

			for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; ++subChunkZ) {
				int noiseZ = subChunkZ;

				int sections = chunk.getSectionsCount() - 1;
				LevelChunkSection section = chunk.getSection(sections);

				for (int subChunkY = 0; subChunkY < this.noiseSizeY; ++subChunkY) {
					int noiseY = subChunkY;

					noiseProviders.forEach(noiseProvider -> noiseProvider.sampleNoiseCorners(noiseX, noiseY, noiseZ));

					for (int subY = 0; subY < this.noiseResolutionVertical; ++subY) {
						int y = subY + (subChunkY + this.noiseMinY) * this.noiseResolutionVertical;
						int localY = y & 0xF;

						int sectionNdx = chunk.getSectionIndex(y);
						if (sections != sectionNdx) {
							sections = sectionNdx;
							section = chunk.getSection(sectionNdx);
						}

						double deltaY = subY / (double) this.noiseResolutionVertical;
						noiseProviders.forEach(noiseProvider -> noiseProvider.sampleNoiseY(deltaY));

						for (int subX = 0; subX < this.noiseResolutionHorizontal; ++subX) {
							int localX = subX + subChunkX * this.noiseResolutionHorizontal;
							int x = startX + localX;

							double deltaX = subX / (double) this.noiseResolutionHorizontal;
							noiseProviders.forEach(noiseProvider -> noiseProvider.sampleNoiseX(deltaX));

							for (int subZ = 0; subZ < this.noiseResolutionHorizontal; ++subZ) {
								int localZ = subZ + subChunkZ * this.noiseResolutionHorizontal;
								int z = startZ + localZ;

								double deltaZ = subZ / (double) this.noiseResolutionHorizontal;
								noiseProviders.forEach(noiseProvider -> noiseProvider.sampleNoiseZ(deltaZ));

								BlockState blockState = blockSources.apply(x, y, z);
								if (blockState.equals(BlockStates.AIR)) continue;

								section.setBlockState(localX, localY, localZ, blockState, false);

								heightmapOcean.update(localX, y, localZ, blockState);
								heightmapSurface.update(localX, y, localZ, blockState);

								this.scheduleFluidTick(chunk, aquiferSampler, mutable.set(x, y, z), blockState);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Generates a heightmap for the chunk containing the given x/z coordinates
	 * and returns to {@link #getHeight(int, int, net.minecraft.world.level.levelgen.Heightmap.Types)}
	 * to cache and return the height.
	 *
	 * @param chunkX x-coordinate in chunk coordinates to sample all y-values for.
	 * @param chunkZ z-coordinate in chunk coordinates to sample all y-values for.
	 * @return A HeightmapChunk, containing an array of ints containing the heights for the entire chunk.
	 */
	private ChunkHeightmap sampleHeightmap(int chunkX, int chunkZ) {
		short minHeight = 32;
		short worldMinY = (short) this.worldMinY;
		short worldTopY = (short) this.worldTopY;

		NoiseProviderBase baseNoiseProvider = this.chunkCacheNoise.get(chunkX, chunkZ);

		short[] heightmapSurface = new short[256];
		short[] heightmapOcean = new short[256];
		short[] heightmapSurfaceFloor = new short[256];

		Arrays.fill(heightmapSurface, minHeight);
		Arrays.fill(heightmapOcean, minHeight);
		Arrays.fill(heightmapSurfaceFloor, worldMinY);

		for (int subChunkX = 0; subChunkX < this.noiseSizeX; ++subChunkX) {
			for (int subChunkZ = 0; subChunkZ < this.noiseSizeZ; ++subChunkZ) {
				for (int subChunkY = 0; subChunkY < this.noiseSizeY; ++subChunkY) {
					baseNoiseProvider.sampleNoiseCornersHeightmap(subChunkX, subChunkY, subChunkZ);

					for (int subY = 0; subY < this.noiseResolutionVertical; ++subY) {
						int y = subY + subChunkY * this.noiseResolutionVertical;
						y += this.worldMinY;

						double deltaY = subY / (double) this.noiseResolutionVertical;
						baseNoiseProvider.sampleNoiseYHeightmap(deltaY);

						for (int subX = 0; subX < this.noiseResolutionHorizontal; ++subX) {
							int x = subX + subChunkX * this.noiseResolutionHorizontal;

							double deltaX = subX / (double) this.noiseResolutionHorizontal;
							baseNoiseProvider.sampleNoiseXHeightmap(deltaX);

							for (int subZ = 0; subZ < this.noiseResolutionHorizontal; ++subZ) {
								int z = subZ + subChunkZ * this.noiseResolutionHorizontal;

								double deltaZ = subZ / (double) this.noiseResolutionHorizontal;
								baseNoiseProvider.sampleNoiseZHeightmap(deltaZ);

								double density = baseNoiseProvider.sampleHeightmap();
								boolean isSolid = density > 0.0;

								short height = (short) (y + 1);
								int ndx = z + x * 16;

								// Capture topmost solid/fluid block height.
								if (y < this.seaLevel || isSolid) {
									heightmapOcean[ndx] = height;
								}

								// Capture topmost solid block height.
								if (isSolid) {
									heightmapSurface[ndx] = height;
								}

								// Capture lowest solid block height.
								// First, set max world height as flag when hitting first solid layer
								// then set the actual height value when hitting first non-solid layer.
								// This handles situations where the bottom of the world may not be solid,
								// i.e. Skylands-style world types.
								if (isSolid && heightmapSurfaceFloor[ndx] == worldMinY) {
									heightmapSurfaceFloor[ndx] = worldTopY;
								}

								if (!isSolid && heightmapSurfaceFloor[ndx] == worldTopY) {
									heightmapSurfaceFloor[ndx] = (short) (height - 1);
								}
							}
						}
					}
				}
			}
		}

		// Construct new heightmap cache from generated heightmap array
		return new ChunkHeightmap(heightmapSurface, heightmapOcean, heightmapSurfaceFloor);
	}

	/**
	 * Creates block source to sample BlockState at block coordinates given base noise provider.
	 *
	 * @param baseNoiseProvider Primary noise provider to sample density noise.
	 * @param weightSampler     Sampler used to add/subtract density if a structure start is at coordinate.
	 * @param aquiferSampler    Sampler used to adjust local water levels for noise caves.
	 * @param blockSource       Default block source
	 * @param noodleCaveSampler Noodle cave density sampler.
	 * @return BlockSource to sample blockstate at x/y/z block coordinates.
	 */
	private BlockSource getBaseBlockSource(
			NoiseProvider baseNoiseProvider,
			Beardifier weightSampler,
			Aquifer aquiferSampler,
			SimpleNoisePos noisePos
	) {
		return (x, y, z) -> {
			double density = baseNoiseProvider.sample();
			double clampedDensity = Mth.clamp(density / 200.0, -1.0, 1.0);

			clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
			clampedDensity += weightSampler.compute(noisePos.set(x, y, z));

			return aquiferSampler.computeSubstance(noisePos, clampedDensity);
		};
	}
}

