package mod.bespectacled.modernbeta.api.world.chunk;

import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.api.world.blocksource.BlockSource;
import mod.bespectacled.modernbeta.api.world.chunk.surface.SurfaceBuilder;
import mod.bespectacled.modernbeta.api.world.spawn.SpawnLocator;
import mod.bespectacled.modernbeta.mixin.AccessorChunkGenerator;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsChunk;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.blocksource.BlockSourceDeepslate;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaGenerationStep;
import mod.bespectacled.modernbeta.world.feature.placement.NoiseBasedCountPlacementModifier;
import net.minecraft.core.Holder;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.FeatureSorter.StepFeatureData;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Aquifer.FluidPicker;
import net.minecraft.world.level.levelgen.Aquifer.FluidStatus;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;
import java.util.Random;
import java.util.concurrent.CompletableFuture;

public abstract class ChunkProvider {
	private final FluidPicker defaultFluidLevelSampler;

	protected final ModernBetaChunkGenerator chunkGenerator;
	protected final long seed;

	protected final Holder<NoiseGeneratorSettings> generatorSettings;
	protected final ModernBetaSettingsChunk chunkSettings;
	protected final Random random;

	protected final WorldgenRandom.Algorithm randomProvider;
	protected final PositionalRandomFactory randomSplitter;
	protected final BlockSourceDeepslate blockSourceDeepslate;

	protected final List<BlockSource> blockSources;
	protected final SurfaceBuilder surfaceBuilder;

	/**
	 * Construct a Modern Beta chunk provider with seed and settings.
	 *
	 * @param chunkGenerator Parent ModernBetaChunkGenerator object used to initialize fields.
	 */
	public ChunkProvider(ModernBetaChunkGenerator chunkGenerator, long seed) {
		this.chunkGenerator = chunkGenerator;
		this.seed = seed;

		this.generatorSettings = chunkGenerator.getGeneratorSettings();
		this.chunkSettings = ModernBetaSettingsChunk.fromCompound(chunkGenerator.getChunkSettings());
		this.random = new Random(this.seed);

		this.defaultFluidLevelSampler = (x, y, z) -> new FluidStatus(this.getSeaLevel(), BlockStates.AIR);
		this.randomProvider = chunkGenerator.getGeneratorSettings().value().getRandomSource();
		this.randomSplitter = this.randomProvider.newInstance(this.seed).forkPositional();
		this.blockSourceDeepslate = new BlockSourceDeepslate(this.chunkSettings, this.randomSplitter);

		this.blockSources = ModernBetaRegistries.BLOCKSOURCE
				.getEntries()
				.stream()
				.map(func -> func.apply(this.chunkSettings, this.randomSplitter))
				.toList();

		this.surfaceBuilder = new SurfaceBuilder(this.chunkGenerator.getBiomeSource());
	}

	/**
	 * Generates base terrain for given chunk and returns it.
	 *
	 * @param blender           TODO
	 * @param structureAccessor
	 * @param chunk
	 * @param noiseConfig       TODO
	 * @return A completed chunk.
	 */
	public abstract CompletableFuture<ChunkAccess> provideChunk(Blender blender, StructureManager structureAccessor, ChunkAccess chunk, RandomState noiseConfig);

	/**
	 * Generates biome-specific surface for given chunk.
	 *
	 * @param region
	 * @param structureAccessor TODO
	 * @param chunk
	 * @param biomeSource
	 * @param noiseConfig       TODO
	 */
	public abstract void provideSurface(WorldGenRegion region, StructureManager structureAccessor, ChunkAccess chunk, ModernBetaBiomeSource biomeSource, RandomState noiseConfig);

	/**
	 * Sample height at given x/z coordinate. Initially generates heightmap for entire chunk,
	 * if chunk containing x/z coordinates has never been sampled.
	 *
	 * @param x    x-coordinate in block coordinates.
	 * @param z    z-coordinate in block coordinates.
	 * @param heightmap Vanilla heightmap type.
	 * @return The y-coordinate of top block at x/z.
	 */
	public abstract int getHeight(int x, int z, Heightmap.Types heightmap);

	/**
	 * Determines whether to skip the chunk for some chunk generation step, depending on the x/z chunk coordinates.
	 *
	 * @param chunkX      x-coordinate in chunk coordinates.
	 * @param chunkZ      z-coordinate in chunk coordinates.
	 * @param step Chunk generation step used for skip context.
	 * @return Whether to skip the chunk.
	 */
	public boolean skipChunk(int chunkX, int chunkZ, ModernBetaGenerationStep step) {
		if (step == ModernBetaGenerationStep.CARVERS) {
			return !this.chunkSettings.useCaves;
		}

		return false;
	}

	/**
	 * Get total world height in blocks, including minimum Y.
	 * (i.e. Returns 320 if bottomY is -64 and topY is 256.)
	 *
	 * @return Total world height in blocks.
	 */
	public int getWorldHeight() {
		return this.generatorSettings.value().noiseSettings().height();
	}

	/**
	 * @return Minimum Y coordinate in block coordinates.
	 */
	public int getWorldMinY() {
		return this.generatorSettings.value().noiseSettings().minY();
	}

	/**
	 * @return World sea level in block coordinates.
	 */
	public int getSeaLevel() {
		return this.generatorSettings.value().seaLevel();
	}

	/**
	 * Get aquifer sampler, for carving for now.
	 *
	 * @return An aquifer sampler.
	 */
	public Aquifer getAquiferSampler(ChunkAccess chunk, RandomState noiseConfig) {
		return Aquifer.createDisabled(this.defaultFluidLevelSampler);
	}

	/**
	 * Get empty fluid level sampler.
	 *
	 * @return Empty FluidLevelSampler.
	 */
	public FluidPicker getFluidLevelSampler() {
		return this.defaultFluidLevelSampler;
	}

	/**
	 * @return Parent ModernBetaChunkGenerator.
	 */
	public ModernBetaChunkGenerator getChunkGenerator() {
		return this.chunkGenerator;
	}

	/**
	 * @return Chunk provider's spawn locator.
	 */
	public SpawnLocator getSpawnLocator() {
		return SpawnLocator.DEFAULT;
	}

	/**
	 * Sets forest density using PerlinOctaveNoise sampler created with world seed.
	 * Checks every placed feature in the biome source feature list,
	 * and if it uses ModernBetaNoiseBasedCountPlacementModifier, replaces the noise sampler.
	 *
//	 * @param forestOctaves PerlinOctaveNoise object used to set forest octaves.
	 */
	public void initForestOctaveNoise() {
		List<StepFeatureData> generationSteps = ((AccessorChunkGenerator) this.chunkGenerator).getIndexedFeaturesListSupplier().get();

		for (StepFeatureData step : generationSteps) {
			List<PlacedFeature> featureList = step.features();

			for (PlacedFeature placedFeature : featureList) {
				List<PlacementModifier> modifiers = placedFeature.placement();

				for (PlacementModifier modifier : modifiers) {
					if (modifier instanceof NoiseBasedCountPlacementModifier noiseModifier) {
						noiseModifier.setOctaves(this.getForestOctaveNoise());
					}
				}
			}
		}
	}

	/**
	 * Samples biome at given biome coordinates.
	 *
	 * @param biomeX       x-coordinate in biome coordinates.
	 * @param biomeY       y-coordinate in biome coordinates.
	 * @param biomeZ       z-coordinate in biome coordinates.
	 * @param noiseSampler
	 * @return A biome.
	 */
	public Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ, Climate.Sampler noiseSampler) {
		return this.chunkGenerator.getBiomeSource().getNoiseBiome(biomeX, biomeY, biomeZ, noiseSampler);
	}

	/**
	 * Creates a ModernBetaChunkNoiseSampler
	 */
	public NoiseChunk createChunkNoiseSampler(ChunkAccess chunk, StructureManager world, Blender blender, RandomState noiseConfig) {
		return NoiseChunk.forChunk(
				chunk,
				noiseConfig,
				Beardifier.forStructuresInChunk(world, chunk.getPos()),
				this.generatorSettings.value(),
				this.getFluidLevelSampler(),
				blender
		);
	}

	public ModernBetaSettingsChunk getChunkSettings() {
		return this.chunkSettings;
	}

	/**
	 * Get a new Random object initialized with chunk coordinates for seed, for surface generation.
	 *
	 * @param chunkX x-coordinate in chunk coordinates.
	 * @param chunkZ z-coordinate in chunk coordinates.
	 * @return New Random object initialized with chunk coordinates for seed.
	 */
	protected Random createSurfaceRandom(int chunkX, int chunkZ) {
		long seed = (long) chunkX * 0x4f9939f508L + (long) chunkZ * 0x1ef1565bd5L;

		return new Random(seed);
	}

	/**
	 * Get Perlin octave noise sampler for tree placement.
	 *
	 * @return Perlin octave noise sampler.
	 */
	protected PerlinOctaveNoise getForestOctaveNoise() {
		return new PerlinOctaveNoise(new Random(this.seed), 8, true);
	}
}