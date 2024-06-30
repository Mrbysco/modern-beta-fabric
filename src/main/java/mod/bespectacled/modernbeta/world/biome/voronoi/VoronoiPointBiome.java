package mod.bespectacled.modernbeta.world.biome.voronoi;

import mod.bespectacled.modernbeta.util.NbtCompoundBuilder;
import mod.bespectacled.modernbeta.util.NbtListBuilder;
import mod.bespectacled.modernbeta.util.NbtReader;
import mod.bespectacled.modernbeta.util.NbtTags;
import mod.bespectacled.modernbeta.util.NbtUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

import java.util.List;

public record VoronoiPointBiome(String biome, String oceanBiome, String deepOceanBiome, double temp, double rain,
                                double weird) {
	public static final VoronoiPointBiome DEFAULT = new VoronoiPointBiome("modern_beta:beta_forest", "modern_beta:beta_ocean", 0.5, 0.5);

	public VoronoiPointBiome(String biome, String oceanBiome, double temp, double rain) {
		this(biome, oceanBiome, oceanBiome, temp, rain, 0.5);
	}

	public static List<VoronoiPointBiome> listFromReader(NbtReader reader, List<VoronoiPointBiome> alternate) {
		if (reader.contains(NbtTags.VORONOI_POINTS)) {
			return reader.readListOrThrow(NbtTags.VORONOI_POINTS)
					.stream()
					.map(e -> {
						CompoundTag point = NbtUtil.toCompoundOrThrow(e);
						NbtReader pointReader = new NbtReader(point);

						String biome = pointReader.readStringOrThrow(NbtTags.BIOME);
						String oceanBiome = pointReader.readStringOrThrow(NbtTags.OCEAN_BIOME);
						String deepOceanBiome = pointReader.readStringOrThrow(NbtTags.DEEP_OCEAN_BIOME);

						double temp = pointReader.readDoubleOrThrow(NbtTags.TEMP);
						double rain = pointReader.readDoubleOrThrow(NbtTags.RAIN);
						double weird = pointReader.readDouble(NbtTags.WEIRD, 0.5);

						return new VoronoiPointBiome(
								biome,
								oceanBiome,
								deepOceanBiome,
								temp,
								rain,
								weird
						);
					})
					.toList();
		}

		return List.copyOf(alternate);
	}

	public static ListTag listToNbt(List<VoronoiPointBiome> points) {
		NbtListBuilder builder = new NbtListBuilder();
		points.forEach(p -> builder.add(p.toCompound()));

		return builder.build();
	}

	public CompoundTag toCompound() {
		return new NbtCompoundBuilder()
				.putString(NbtTags.BIOME, this.biome)
				.putString(NbtTags.OCEAN_BIOME, this.oceanBiome)
				.putString(NbtTags.DEEP_OCEAN_BIOME, this.deepOceanBiome)
				.putDouble(NbtTags.TEMP, this.temp)
				.putDouble(NbtTags.RAIN, this.rain)
				.putDouble(NbtTags.WEIRD, this.weird)
				.build();
	}
}
