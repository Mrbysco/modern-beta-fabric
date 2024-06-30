package mod.bespectacled.modernbeta.api.world.biome.climate;

public interface ClimateSamplerSky {
	/**
	 * Sample temperature value from given coordinates,
	 * to use to provide sky color.
	 *
	 * @param x x-coordinate in block coordinates.
	 * @param z z-coordinate in block coordinates.
	 * @return A temperature value sampled at position.
	 */
	double sampleSky(int x, int z);

	/**
	 * Indicate to client world mixin whether to sample climate values for sky color.
	 *
	 * @return Supplier for whether to use climate values for biome tinting.
	 */
	default boolean useSkyColor() {
		return false;
	}
}
