package mod.bespectacled.modernbeta.api.world.biome;

import mod.bespectacled.modernbeta.settings.ModernBetaSettingsBiome;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.biome.Biome;

import java.util.List;

public abstract class BiomeProvider {
	protected final ModernBetaSettingsBiome settings;
	protected final HolderGetter<Biome> biomeRegistry;
	protected final long seed;

	/**
	 * Constructs a Modern Beta biome provider initialized with seed.
	 * Additional settings are supplied in NbtCompound parameter.
	 *
	 * @param settings      Biome settings.
	 * @param biomeRegistry Minecraft biome registry.
	 */
	public BiomeProvider(CompoundTag settings, HolderGetter<Biome> biomeRegistry, long seed) {
		this.settings = ModernBetaSettingsBiome.fromCompound(settings);
		this.biomeRegistry = biomeRegistry;
		this.seed = seed;
	}

	/**
	 * Gets a biome for biome source at given biome coordinates.
	 * Note that a single biome coordinate unit equals 4 blocks.
	 *
	 * @param biomeX x-coordinate in biome coordinates.
	 * @param biomeY y-coordinate in biome coordinates.
	 * @param biomeZ z-coordinate in biome coordinates.
	 * @return A biome at given biome coordinates.
	 */
	public abstract Holder<Biome> getBiome(int biomeX, int biomeY, int biomeZ);

	/**
	 * Gets a list of biomes for biome source, for the purpose of locating structures, etc.
	 *
	 * @return A list of biomes.
	 */
	public List<Holder<Biome>> getBiomes() {
		return List.of();
	}
}
