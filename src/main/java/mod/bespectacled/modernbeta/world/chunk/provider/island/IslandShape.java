package mod.bespectacled.modernbeta.world.chunk.provider.island;

import net.minecraft.util.Mth;

public enum IslandShape {
	CIRCLE("circle", (noiseX, noiseZ) -> Mth.sqrt(noiseX * noiseX + noiseZ * noiseZ)),
	SQUARE("square", (noiseX, noiseZ) -> Math.max(Math.abs(noiseX), Math.abs(noiseZ))),
	DIAMOND("diamond", (noiseX, noiseZ) -> Math.abs(noiseX) + Math.abs(noiseZ));

	private final String id;
	private final DistanceProvider provider;

	private IslandShape(String id, DistanceProvider provider) {
		this.id = id;
		this.provider = provider;
	}

	public String getId() {
		return this.id;
	}

	public double getDistance(int noiseX, int noiseZ) {
		return this.provider.apply(noiseX, noiseZ);
	}

	public static IslandShape fromId(String id) {
		for (IslandShape shape : IslandShape.values()) {
			if (shape.id.equalsIgnoreCase(id)) {
				return shape;
			}
		}

		throw new IllegalArgumentException("No Island Shape matching id: " + id);
	}

	@FunctionalInterface
	public static interface DistanceProvider {
		double apply(int noiseX, int noiseZ);
	}
}
