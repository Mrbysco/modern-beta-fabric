package mod.bespectacled.modernbeta.util.noise;

import net.minecraft.world.level.levelgen.DensityFunction;

public class SimpleNoisePos implements DensityFunction.FunctionContext {
	private int blockX;
	private int blockY;
	private int blockZ;

	public SimpleNoisePos() {
		this.blockX = 0;
		this.blockY = 0;
		this.blockZ = 0;
	}

	public SimpleNoisePos(int blockX, int blockY, int blockZ) {
		this.blockX = blockX;
		this.blockY = blockY;
		this.blockZ = blockZ;
	}

	public SimpleNoisePos set(int blockX, int blockY, int blockZ) {
		this.blockX = blockX;
		this.blockY = blockY;
		this.blockZ = blockZ;

		return this;
	}

	@Override
	public int blockX() {
		return this.blockX;
	}

	@Override
	public int blockY() {
		return this.blockY;
	}

	@Override
	public int blockZ() {
		return this.blockZ;
	}

}
