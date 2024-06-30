package mod.bespectacled.modernbeta.world.biome;

import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.api.world.biome.BiomeProvider;
import mod.bespectacled.modernbeta.api.world.biome.BiomeResolverBlock;
import mod.bespectacled.modernbeta.api.world.biome.BiomeResolverOcean;
import mod.bespectacled.modernbeta.api.world.cavebiome.CaveBiomeProvider;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsBiome;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsCaveBiome;
import mod.bespectacled.modernbeta.world.biome.injector.BiomeInjector.BiomeInjectionStep;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.Climate.Sampler;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ModernBetaBiomeSource extends BiomeSource {
	public static final MapCodec<ModernBetaBiomeSource> CODEC = RecordCodecBuilder.mapCodec(instance -> instance
			.group(
					RegistryOps.retrieveGetter(Registries.BIOME),
					CompoundTag.CODEC.fieldOf("provider_settings").forGetter(biomeSource -> biomeSource.biomeSettings),
					CompoundTag.CODEC.fieldOf("cave_provider_settings").forGetter(biomeSource -> biomeSource.caveBiomeSettings)
			).apply(instance, (instance).stable(ModernBetaBiomeSource::new)));

	private final HolderGetter<Biome> biomeRegistry;
	private final CompoundTag biomeSettings;
	private final CompoundTag caveBiomeSettings;

	private BiomeProvider biomeProvider;
	private CaveBiomeProvider caveBiomeProvider;

	private ModernBetaChunkGenerator chunkGenerator;

	public ModernBetaBiomeSource(
			HolderGetter<Biome> biomeRegistry,
			CompoundTag biomeSettings,
			CompoundTag caveBiomeSettings
	) {
		super();

		this.biomeRegistry = biomeRegistry;
		this.biomeSettings = biomeSettings;
		this.caveBiomeSettings = caveBiomeSettings;
	}

	public void initProvider(long seed) {
		ModernBetaSettingsBiome biomeSettings = ModernBetaSettingsBiome.fromCompound(this.biomeSettings);
		ModernBetaSettingsCaveBiome caveBiomeSettings = ModernBetaSettingsCaveBiome.fromCompound(this.caveBiomeSettings);

		this.biomeProvider = ModernBetaRegistries.BIOME
				.get(biomeSettings.biomeProvider)
				.apply(this.biomeSettings, this.biomeRegistry, seed);

		this.caveBiomeProvider = ModernBetaRegistries.CAVE_BIOME
				.get(caveBiomeSettings.biomeProvider)
				.apply(this.caveBiomeSettings, this.biomeRegistry, seed);
	}

	@Override
	public Holder<Biome> getNoiseBiome(int biomeX, int biomeY, int biomeZ, Climate.Sampler noiseSampler) {
		return this.biomeProvider.getBiome(biomeX, biomeY, biomeZ);
	}

	@Override
	public Set<Holder<Biome>> getBiomesWithin(int startX, int startY, int startZ, int radius, Sampler noiseSampler) {
		if (this.chunkGenerator == null)
			return super.getBiomesWithin(startX, startY, startZ, radius, noiseSampler);

		int minX = QuartPos.fromBlock(startX - radius);
		int minZ = QuartPos.fromBlock(startZ - radius);

		int maxX = QuartPos.fromBlock(startX + radius);
		int maxZ = QuartPos.fromBlock(startZ + radius);

		int rangeX = maxX - minX + 1;
		int rangeZ = maxZ - minZ + 1;

		HashSet<Holder<Biome>> set = Sets.newHashSet();
		for (int localZ = 0; localZ < rangeZ; ++localZ) {
			for (int localX = 0; localX < rangeX; ++localX) {
				int biomeX = minX + localX;
				int biomeZ = minZ + localZ;

				int x = biomeX << 2;
				int z = biomeZ << 2;
				int y = this.chunkGenerator.getHeight(x, z, Heightmap.Types.OCEAN_FLOOR_WG);

				set.add(this.chunkGenerator.getBiomeInjector().getBiomeAtBlock(x, y, z, noiseSampler, BiomeInjectionStep.ALL));
			}
		}

		return set;
	}

	@Override
	public Pair<BlockPos, Holder<Biome>> findClosestBiome3d(
			BlockPos origin,
			int radius,
			int horizontalBlockCheckInterval,
			int verticalBlockCheckInterval,
			Predicate<Holder<Biome>> predicate,
			Climate.Sampler noiseSampler,
			LevelReader world
	) {
		if (this.chunkGenerator == null) {
			return super.findClosestBiome3d(
					origin,
					radius,
					horizontalBlockCheckInterval,
					verticalBlockCheckInterval,
					predicate,
					noiseSampler,
					world
			);
		}

		Set<Holder<Biome>> biomeSet = this.possibleBiomes()
				.stream()
				.filter(predicate)
				.collect(Collectors.toUnmodifiableSet());

		if (biomeSet.isEmpty()) {
			return null;
		}

		int searchRadius = Math.floorDiv(radius, horizontalBlockCheckInterval);
		int[] sections = Mth
				.outFromOrigin(origin.getY(), world.getMinBuildHeight() + 1, world.getMaxBuildHeight(), verticalBlockCheckInterval)
				.toArray();

		for (BlockPos.MutableBlockPos mutable : BlockPos.spiralAround(BlockPos.ZERO, searchRadius, Direction.EAST, Direction.SOUTH)) {
			int x = origin.getX() + mutable.getX() * horizontalBlockCheckInterval;
			int z = origin.getZ() + mutable.getZ() * horizontalBlockCheckInterval;

			int biomeX = QuartPos.fromBlock(x);
			int biomeZ = QuartPos.fromBlock(z);

			for (int y : sections) {
				int biomeY = QuartPos.fromBlock(y);

				Holder<Biome> biome = this.chunkGenerator
						.getBiomeInjector()
						.getBiome(biomeX, biomeY, biomeZ, noiseSampler, BiomeInjectionStep.ALL);

				if (!biomeSet.contains(biome)) continue;

				return Pair.of(new BlockPos(x, y, z), biome);
			}
		}

		return null;
	}

	public Holder<Biome> getOceanBiome(int biomeX, int biomeY, int biomeZ) {
		if (this.biomeProvider instanceof BiomeResolverOcean biomeResolverOcean)
			return biomeResolverOcean.getOceanBiome(biomeX, biomeY, biomeZ);

		return this.biomeProvider.getBiome(biomeX, biomeY, biomeZ);
	}

	public Holder<Biome> getDeepOceanBiome(int biomeX, int biomeY, int biomeZ) {
		if (this.biomeProvider instanceof BiomeResolverOcean biomeResolverOcean)
			return biomeResolverOcean.getDeepOceanBiome(biomeX, biomeY, biomeZ);

		return this.biomeProvider.getBiome(biomeX, biomeY, biomeZ);
	}

	public Holder<Biome> getCaveBiome(int biomeX, int biomeY, int biomeZ) {
		return this.caveBiomeProvider.getBiome(biomeX, biomeY, biomeZ);
	}

	public Holder<Biome> getBiomeForSpawn(int x, int y, int z) {
		if (this.biomeProvider instanceof BiomeResolverBlock biomeResolver) {
			return biomeResolver.getBiomeBlock(x, y, z);
		}

		return this.biomeProvider.getBiome(x >> 2, y >> 2, z >> 2);
	}

	public Holder<Biome> getBiomeForSurfaceGen(WorldGenRegion region, BlockPos pos) {
		if (this.biomeProvider instanceof BiomeResolverBlock biomeResolver)
			return biomeResolver.getBiomeBlock(pos.getX(), pos.getY(), pos.getZ());

		return region.getBiome(pos);
	}

	public void setChunkGenerator(ModernBetaChunkGenerator chunkGenerator) {
		this.chunkGenerator = chunkGenerator;
	}

	public BiomeProvider getBiomeProvider() {
		return this.biomeProvider;
	}

	public CaveBiomeProvider getCaveBiomeProvider() {
		return this.caveBiomeProvider;
	}

	public CompoundTag getBiomeSettings() {
		return this.biomeSettings;
	}

	public CompoundTag getCaveBiomeSettings() {
		return this.caveBiomeSettings;
	}

	@Override
	protected MapCodec<? extends BiomeSource> codec() {
		return ModernBetaBiomeSource.CODEC;
	}

	@Override
	protected Stream<Holder<Biome>> collectPossibleBiomes() {
		ModernBetaSettingsBiome modernBetaBiomeSettings = ModernBetaSettingsBiome.fromCompound(this.biomeSettings);
		ModernBetaSettingsCaveBiome modernBetaCaveBiomeSettings = ModernBetaSettingsCaveBiome.fromCompound(this.caveBiomeSettings);

		BiomeProvider biomeProvider = ModernBetaRegistries.BIOME
				.get(modernBetaBiomeSettings.biomeProvider)
				.apply(biomeSettings, biomeRegistry, 0L);

		CaveBiomeProvider caveBiomeProvider = ModernBetaRegistries.CAVE_BIOME
				.get(modernBetaCaveBiomeSettings.biomeProvider)
				.apply(caveBiomeSettings, biomeRegistry, 0L);

		List<Holder<Biome>> biomes = new ArrayList<>();
		biomes.addAll(biomeProvider.getBiomes());
		biomes.addAll(caveBiomeProvider.getBiomes());

		return biomes.stream();
	}
}