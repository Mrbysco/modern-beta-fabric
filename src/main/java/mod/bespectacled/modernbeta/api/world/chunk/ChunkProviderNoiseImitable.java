package mod.bespectacled.modernbeta.api.world.chunk;

import mod.bespectacled.modernbeta.api.world.blocksource.BlockSource;
import mod.bespectacled.modernbeta.util.BlockStates;
import mod.bespectacled.modernbeta.util.noise.SimpleNoisePos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Beardifier;

public interface ChunkProviderNoiseImitable {
	/**
	 * Gets blockstate at block coordinates given block and default fluid block.
	 * Simulates a noise density for the purpose of masking terrain around structures.
	 * Used for 2D noise terrain generators (i.e. Infdev 20100227 and Indev terrain generators).
	 *
	 * @param weightSampler Structure weight sampler used to beardify terrain for structures.
	 * @param noisePos      Pos object used for weightSampler.
	 * @param blockHolder   Supplier that gets current original block.
	 * @param defaultBlock  Default base block.
	 * @param defaultFluid  Default fluid block.
	 * @return A blockstate.
	 */
	default BlockSource getBaseBlockSource(
			Beardifier weightSampler,
			SimpleNoisePos noisePos,
			BlockHolder blockHolder,
			Block defaultBlock,
			Block defaultFluid
	) {
		return (x, y, z) -> {
			Block originalBlock = blockHolder.getBlock();

			boolean isFluid = originalBlock == defaultFluid;
			boolean isAir = originalBlock == Blocks.AIR;
			boolean isSolid = !isFluid && !isAir;

			double density = !isSolid ? -25D : 25D;

			double clampedDensity = Mth.clamp(density / 200.0, -1.0, 1.0);
			clampedDensity = clampedDensity / 2.0 - clampedDensity * clampedDensity * clampedDensity / 24.0;
			clampedDensity += weightSampler.compute(noisePos.set(x, y, z));

			BlockState blockState = BlockStates.AIR;

			if (clampedDensity > 0.0) {
				blockState = null;

				// Handle structures generating over water/air
				if (!isSolid)
					blockState = defaultBlock.defaultBlockState();

			} else if (clampedDensity <= 0.0) {
				// Handle original fluid blocks
				if (isFluid)
					blockState = null;

				// Handle structures generating inside pre-existing terrain
				if (isSolid)
					blockState = BlockStates.AIR;
			}

			return blockState;
		};
	}

	default BlockSource getActualBlockSource(BlockHolder blockHolder) {
		return (x, y, z) -> blockHolder.getBlock().defaultBlockState();
	}

	public static class BlockHolder {
		private Block block;

		public BlockHolder() {
			this.block = Blocks.AIR;
		}

		public Block getBlock() {
			return this.block;
		}

		public void setBlock(Block block) {
			this.block = block;
		}
	}
}
