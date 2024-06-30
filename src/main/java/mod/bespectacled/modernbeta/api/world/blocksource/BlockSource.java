package mod.bespectacled.modernbeta.api.world.blocksource;

import net.minecraft.world.level.block.state.BlockState;

@FunctionalInterface
public interface BlockSource {
	public static final BlockSource DEFAULT = (x, y, z) -> null;

	BlockState apply(int x, int y, int z);
}
