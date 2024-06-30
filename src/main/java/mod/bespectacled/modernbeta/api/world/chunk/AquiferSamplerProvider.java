package mod.bespectacled.modernbeta.api.world.chunk;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.util.BlockStates;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.Aquifer.FluidPicker;
import net.minecraft.world.level.levelgen.Aquifer.FluidStatus;
import net.minecraft.world.level.levelgen.NoiseChunk;
import net.minecraft.world.level.levelgen.NoiseRouter;
import net.minecraft.world.level.levelgen.PositionalRandomFactory;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;

public class AquiferSamplerProvider {
	private static final int FAR_LANDS_BOUNDARY = 12550821;

	private final NoiseRouter noiseRouter;
	private final PositionalRandomFactory randomSplitter;

	private final FluidPicker fluidLevelSampler;
	private final FluidPicker lavalessFluidLevelSampler;

	private final NoiseChunk chunkSampler;

	private final int worldMinY;
	private final int worldHeight;
	private final int noiseResolutionVertical;

	private final boolean generateAquifers;

	public AquiferSamplerProvider(
			NoiseRouter noiseRouter,
			BlockState defaultFluid,
			int seaLevel,
			int lavaLevel
	) {
		this(
				noiseRouter,
				new SingleThreadedRandomSource(-1).forkPositional(),
				null,
				defaultFluid,
				seaLevel,
				lavaLevel,
				0,
				0,
				0,
				false
		);
	}

	public AquiferSamplerProvider(
			NoiseRouter noiseRouter,
			PositionalRandomFactory randomSplitter,
			NoiseChunk chunkSampler,
			BlockState defaultFluid,
			int seaLevel,
			int lavaLevel,
			int worldMinY,
			int worldHeight,
			int noiseResolutionVertical,
			boolean generateAquifers
	) {
		this.noiseRouter = noiseRouter;
		this.randomSplitter = randomSplitter.fromHashOf(ModernBeta.createId("aquifer")).forkPositional();

		FluidStatus lavaFluidLevel = new FluidStatus(lavaLevel, BlockStates.LAVA); // Vanilla: -54
		FluidStatus seaFluidLevel = new FluidStatus(seaLevel, defaultFluid);

		this.fluidLevelSampler = (x, y, z) -> {
			// Do not generate lava past Far Lands boundary
			if (Math.abs(x) >= FAR_LANDS_BOUNDARY || Math.abs(z) >= FAR_LANDS_BOUNDARY)
				return seaFluidLevel;

			return y < lavaLevel ? lavaFluidLevel : seaFluidLevel;
		};

		this.lavalessFluidLevelSampler = (x, y, z) -> seaFluidLevel;

		this.chunkSampler = chunkSampler;

		this.worldMinY = worldMinY;
		this.worldHeight = worldHeight;
		this.noiseResolutionVertical = noiseResolutionVertical;

		this.generateAquifers = generateAquifers;
	}

	public Aquifer provideAquiferSampler(ChunkAccess chunk) {
		if (!this.generateAquifers) {
			return Aquifer.createDisabled(this.lavalessFluidLevelSampler);
		}

		int minY = Math.max(this.worldMinY, chunk.getMinBuildHeight());
		int topY = Math.min(this.worldMinY + this.worldHeight, chunk.getMaxBuildHeight());

		int noiseMinY = Mth.floorDiv(minY, this.noiseResolutionVertical);
		int noiseTopY = Mth.floorDiv(topY - minY, this.noiseResolutionVertical);

		return Aquifer.create(
				this.chunkSampler,
				chunk.getPos(),
				this.noiseRouter,
				this.randomSplitter,
				noiseMinY * this.noiseResolutionVertical,
				noiseTopY * this.noiseResolutionVertical,
				this.fluidLevelSampler
		);
	}
}
