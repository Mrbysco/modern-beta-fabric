package mod.bespectacled.modernbeta.world.biome.provider.climate;

import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtReader;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

import java.util.LinkedHashMap;
import java.util.Map;

public record ClimateMapping(String biome, String oceanBiome, String deepOceanBiome) {
	public ClimateMapping(String biome, String oceanBiome) {
		this(biome, oceanBiome, oceanBiome);
	}

	public ResourceKey<Biome> getBiome(ClimateType type) {
		return switch (type) {
			case LAND -> keyOf(this.biome);
			case OCEAN -> keyOf(this.oceanBiome);
			case DEEP_OCEAN -> keyOf(this.deepOceanBiome);
		};
	}

	public CompoundTag toCompound() {
		return new NbtCompoundBuilder()
				.putString(NbtTags.BIOME, this.biome)
				.putString(NbtTags.OCEAN_BIOME, this.oceanBiome)
				.putString(NbtTags.DEEP_OCEAN_BIOME, this.deepOceanBiome)
				.build();
	}

	public static ClimateMapping fromCompound(CompoundTag compound) {
		String biome = NbtUtil.readStringOrThrow(NbtTags.BIOME, compound);
		String oceanBiome = NbtUtil.readStringOrThrow(NbtTags.OCEAN_BIOME, compound);
		String deepOceanBiome = NbtUtil.readStringOrThrow(NbtTags.DEEP_OCEAN_BIOME, compound);

		return new ClimateMapping(biome, oceanBiome, deepOceanBiome);
	}

	public static Map<String, ClimateMapping> mapFromReader(NbtReader reader, Map<String, ClimateMapping> alternate) {
		if (reader.contains(NbtTags.CLIMATE_MAPPINGS)) {
			Map<String, ClimateMapping> map = new LinkedHashMap<>();

			CompoundTag biomes = reader.readCompoundOrThrow(NbtTags.CLIMATE_MAPPINGS);
			NbtReader biomeReader = new NbtReader(biomes);

			biomes.getAllKeys().forEach(key -> map.put(key, ClimateMapping.fromCompound(biomeReader.readCompoundOrThrow(key))));

			return map;
		}

		return Map.copyOf(alternate);
	}

	public static CompoundTag mapToNbt(Map<String, ClimateMapping> mappings) {
		NbtCompoundBuilder builder = new NbtCompoundBuilder();
		mappings.keySet().forEach(key -> builder.putCompound(key, mappings.get(key).toCompound()));

		return builder.build();
	}

	private static ResourceKey<Biome> keyOf(String id) {
		return ResourceKey.create(Registries.BIOME, ResourceLocation.tryParse(id));
	}
}
