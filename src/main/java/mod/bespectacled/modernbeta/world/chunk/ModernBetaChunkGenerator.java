package mod.bespectacled.modernbeta.world.chunk;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProvider;
import mod.bespectacled.modernbeta.mixin.AccessorNoiseChunk;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsChunk;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.biome.injector.BiomeInjector;
import mod.bespectacled.modernbeta.world.biome.injector.BiomeInjector.BiomeInjectionStep;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.level.levelgen.blending.Blender;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;

import java.util.concurrent.CompletableFuture;

public class ModernBetaChunkGenerator extends NoiseBasedChunkGenerator {
	public static final MapCodec<ModernBetaChunkGenerator> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
			BiomeSource.CODEC.fieldOf("biome_source").forGetter(generator -> generator.biomeSource),
			NoiseGeneratorSettings.CODEC.fieldOf("settings").forGetter(generator -> generator.settings),
			CompoundTag.CODEC.fieldOf("provider_settings").forGetter(generator -> generator.chunkSettings)
	).apply(instance, instance.stable(ModernBetaChunkGenerator::new)));

	private final Holder<NoiseGeneratorSettings> settings;
	private final CompoundTag chunkSettings;
	private final BiomeInjector biomeInjector;

	private ChunkProvider chunkProvider;

	public ModernBetaChunkGenerator(
			BiomeSource biomeSource,
			Holder<NoiseGeneratorSettings> settings,
			CompoundTag chunkProviderSettings
	) {
		super(biomeSource, settings);

		this.settings = settings;
		this.chunkSettings = chunkProviderSettings;
		this.biomeInjector = this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource ?
				new BiomeInjector(this, modernBetaBiomeSource) :
				null;

		if (this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
			modernBetaBiomeSource.setChunkGenerator(this);
		}
	}

	public void initProvider(long seed) {
		ModernBetaSettingsChunk chunkSettings = ModernBetaSettingsChunk.fromCompound(this.chunkSettings);

		this.chunkProvider = ModernBetaRegistries.CHUNK
				.get(chunkSettings.chunkProvider)
				.apply(this, seed);

		this.chunkProvider.initForestOctaveNoise();
	}

	@Override
	public CompletableFuture<ChunkAccess> createBiomes(RandomState noiseConfig, Blender blender, StructureManager structureAccessor, ChunkAccess chunk) {
		return CompletableFuture.<ChunkAccess>supplyAsync(Util.wrapThreadWithTaskName("init_biomes", () -> {
			NoiseChunk noiseSampler = chunk.getOrCreateNoiseChunk(c -> this.createNoiseChunk(c, structureAccessor, blender, noiseConfig));
			chunk.fillBiomesFromNoise(this.biomeSource,
					((AccessorNoiseChunk) noiseSampler).invokeCachedClimateSampler(noiseConfig.router(), this.settings.value().spawnTarget()));

			return chunk;

		}), Util.backgroundExecutor());
	}

	@Override
	public CompletableFuture<ChunkAccess> fillFromNoise(Blender blender, RandomState randomState, StructureManager structureManager, ChunkAccess chunk) {
		CompletableFuture<ChunkAccess> completedChunk = this.chunkProvider.provideChunk(Blender.empty(), structureManager, chunk, randomState);
		return completedChunk;
	}

	@Override
	public void buildSurface(WorldGenRegion chunkRegion, StructureManager structureAccessor, RandomState noiseConfig, ChunkAccess chunk) {
		this.injectBiomes(chunk, noiseConfig.sampler(), BiomeInjectionStep.PRE);

		if (!this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ModernBetaGenerationStep.SURFACE)) {
			if (this.biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
				this.chunkProvider.provideSurface(chunkRegion, structureAccessor, chunk, modernBetaBiomeSource, noiseConfig);
			} else {
				super.buildSurface(chunkRegion, structureAccessor, noiseConfig, chunk);
			}
		}

		this.injectBiomes(chunk, noiseConfig.sampler(), BiomeInjectionStep.POST);
	}

	@Override
	public void applyCarvers(WorldGenRegion chunkRegion, long seed, RandomState noiseConfig, BiomeManager biomeAccess, StructureManager structureAccessor, ChunkAccess chunk, GenerationStep.Carving carverStep) {
		if (this.chunkProvider.skipChunk(chunk.getPos().x, chunk.getPos().z, ModernBetaGenerationStep.CARVERS)) return;

		BiomeManager biomeAccessWithSource = biomeAccess.withDifferentSource((biomeX, biomeY, biomeZ) -> this.biomeSource.getNoiseBiome(biomeX, biomeY, biomeZ, noiseConfig.sampler()));
		ChunkPos chunkPos = chunk.getPos();

		int mainChunkX = chunkPos.x;
		int mainChunkZ = chunkPos.z;

		Aquifer aquiferSampler = this.chunkProvider.getAquiferSampler(chunk, noiseConfig);

		// Chunk Noise Sampler used to sample surface level
		NoiseChunk chunkNoiseSampler = chunk.getOrCreateNoiseChunk(c -> this.createNoiseChunk(c, structureAccessor, Blender.of(chunkRegion), noiseConfig));

		CarvingContext carverContext = new CarvingContext(this, chunkRegion.registryAccess(), chunk.getHeightAccessorForGeneration(), chunkNoiseSampler, noiseConfig, this.settings.value().surfaceRule());
		CarvingMask carvingMask = ((ProtoChunk) chunk).getOrCreateCarvingMask(carverStep);

		SingleThreadedRandomSource random = new SingleThreadedRandomSource(seed);
		long l = (random.nextLong() / 2L) * 2L + 1L;
		long l1 = (random.nextLong() / 2L) * 2L + 1L;

		for (int chunkX = mainChunkX - 8; chunkX <= mainChunkX + 8; ++chunkX) {
			for (int chunkZ = mainChunkZ - 8; chunkZ <= mainChunkZ + 8; ++chunkZ) {
				ChunkPos carverPos = new ChunkPos(chunkX, chunkZ);
				ChunkAccess carverChunk = chunkRegion.getChunk(carverPos.x, carverPos.z);

				@SuppressWarnings("deprecation")
				BiomeGenerationSettings genSettings = carverChunk.carverBiome(() -> this.getBiomeGenerationSettings(
						this.biomeSource.getNoiseBiome(QuartPos.fromBlock(carverPos.getMinBlockX()), 0, QuartPos.fromBlock(carverPos.getMinBlockZ()), noiseConfig.sampler()))
				);
				Iterable<Holder<ConfiguredWorldCarver<?>>> carverList = genSettings.getCarvers(carverStep);

				for (Holder<ConfiguredWorldCarver<?>> carverEntry : carverList) {
					ConfiguredWorldCarver<?> configuredCarver = carverEntry.value();
					random.setSeed((long) chunkX * l + (long) chunkZ * l1 ^ seed);

					if (configuredCarver.isStartChunk(random)) {
						configuredCarver.carve(carverContext, chunk, biomeAccessWithSource::getBiome, random, aquiferSampler, carverPos, carvingMask);
					}
				}
			}
		}
	}

	@Override
	public void applyBiomeDecoration(WorldGenLevel world, ChunkAccess chunk, StructureManager structureAccessor) {
		ChunkPos pos = chunk.getPos();

		if (this.chunkProvider.skipChunk(pos.x, pos.z, ModernBetaGenerationStep.FEATURES))
			return;

		super.applyBiomeDecoration(world, chunk, structureAccessor);
	}

	@Override
	public void spawnOriginalMobs(WorldGenRegion region) {
		ChunkPos pos = region.getCenter();

		if (this.chunkProvider.skipChunk(pos.x, pos.z, ModernBetaGenerationStep.ENTITY_SPAWN))
			return;

		super.spawnOriginalMobs(region);
	}

	@Override
	public int getBaseHeight(int x, int z, Heightmap.Types type, LevelHeightAccessor world, RandomState noiseConfig) {
		return this.chunkProvider.getHeight(x, z, type);
	}

	public int getHeight(int x, int z, Heightmap.Types type) {
		return this.chunkProvider.getHeight(x, z, type);
	}

	@Override
	public NoiseColumn getBaseColumn(int x, int z, LevelHeightAccessor world, RandomState noiseConfig) {
		int height = this.chunkProvider.getHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG);
		int worldHeight = this.chunkProvider.getWorldHeight();
		int minY = this.chunkProvider.getWorldMinY();

		BlockState[] column = new BlockState[worldHeight];

		for (int y = worldHeight - 1; y >= 0; --y) {
			int worldY = y + minY;

			if (worldY > height) {
				if (worldY > this.getSeaLevel())
					column[y] = BlockStates.AIR;
				else
					column[y] = this.settings.value().defaultFluid();
			} else {
				column[y] = this.settings.value().defaultBlock();
			}
		}

		return new NoiseColumn(minY, column);
	}

	@Override
	public int getGenDepth() {
		// TODO: Causes issue with YOffset.BelowTop decorator (i.e. ORE_COAL_UPPER), find some workaround.
		// Affects both getWorldHeight() and getMinimumY().
		// See: MC-236933 and MC-236723
		if (this.chunkProvider == null)
			return this.getGeneratorSettings().value().noiseSettings().height();

		return this.chunkProvider.getWorldHeight();
	}

	@Override
	public int getMinY() {
		if (this.chunkProvider == null)
			return this.getGeneratorSettings().value().noiseSettings().minY();

		return this.chunkProvider.getWorldMinY();
	}

	@Override
	public int getSeaLevel() {
		return this.chunkProvider.getSeaLevel();
	}

	public NoiseChunk createNoiseChunk(ChunkAccess chunk, StructureManager world, Blender blender, RandomState noiseConfig) {
		return NoiseChunk.forChunk(
				chunk,
				noiseConfig,
				Beardifier.forStructuresInChunk(world, chunk.getPos()),
				this.settings.value(),
				this.chunkProvider.getFluidLevelSampler(),
				blender
		);
	}

	public Holder<NoiseGeneratorSettings> getGeneratorSettings() {
		return this.settings;
	}

	public ChunkProvider getChunkProvider() {
		return this.chunkProvider;
	}

	public CompoundTag getChunkSettings() {
		return this.chunkSettings;
	}

	public BiomeInjector getBiomeInjector() {
		return this.biomeInjector;
	}

	@Override
	protected MapCodec<? extends ChunkGenerator> codec() {
		return ModernBetaChunkGenerator.CODEC;
	}

	private void injectBiomes(ChunkAccess chunk, Sampler noiseSampler, BiomeInjectionStep step) {
		if (this.biomeInjector != null) {
			this.biomeInjector.injectBiomes(chunk, noiseSampler, step);
		}
	}

	public static void register() {
		Registry.register(BuiltInRegistries.CHUNK_GENERATOR, ModernBeta.createId(ModernBeta.MOD_ID), CODEC);
	}
}
