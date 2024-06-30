package mod.bespectacled.modernbeta.api.world.biome;

import net.minecraft.core.Holder;
import net.minecraft.world.level.biome.Biome;

public interface BiomeResolverOcean {
	/**
	 * Gets an ocean biome to overwrite the original biome at given biome coordinates.
	 *
	 * @param biomeX x-coordinate in biome coordinates.
	 * @param biomeY y-coordinate in biome coordinates.
	 * @param biomeZ z-coordinate in biome coordinates.
	 * @return A biome at given biome coordinates. May return null, in which case original biome is not replaced.
	 */
	Holder<Biome> getOceanBiome(int biomeX, int biomeY, int biomeZ);

	/**
	 * Gets a deep ocean biome to overwrite the original biome at given biome coordinates.
	 *
	 * @param biomeX x-coordinate in biome coordinates.
	 * @param biomeY y-coordinate in biome coordinates.
	 * @param biomeZ z-coordinate in biome coordinates.
	 * @return A biome at given biome coordinates. May return null, in which case original biome is not replaced.
	 */
	default Holder<Biome> getDeepOceanBiome(int biomeX, int biomeY, int biomeZ) {
		return this.getOceanBiome(biomeX, biomeY, biomeZ);
	}
}
