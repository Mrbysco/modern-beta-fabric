package mod.bespectacled.modernbeta.world.feature.placement;

import mod.bespectacled.modernbeta.util.noise.PerlinOctaveNoise;
import mod.bespectacled.modernbeta.world.feature.placement.noise.NoiseBasedCount;
import mod.bespectacled.modernbeta.world.feature.placement.noise.NoiseBasedCountBeta;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;
import net.minecraft.world.level.levelgen.placement.RepeatingPlacement;

public abstract class NoiseBasedCountPlacementModifier extends RepeatingPlacement {
	protected final int count;
	protected final double extraChance;
	protected final int extraCount;

	protected NoiseBasedCount noiseDecorator;

	protected NoiseBasedCountPlacementModifier(int count, double extraChance, int extraCount) {
		this.count = count;
		this.extraChance = extraChance;
		this.extraCount = extraCount;

		this.noiseDecorator = new NoiseBasedCountBeta(new SingleThreadedRandomSource(0L));
	}

	@Override
	protected int count(RandomSource random, BlockPos pos) {
		int chunkX = pos.getX() >> 4;
		int chunkZ = pos.getZ() >> 4;

		return this.noiseDecorator.sample(chunkX, chunkZ, random) + this.count + ((random.nextFloat() < this.extraChance) ? this.extraCount : 0);
	}

	public abstract void setOctaves(PerlinOctaveNoise octaves);
}
