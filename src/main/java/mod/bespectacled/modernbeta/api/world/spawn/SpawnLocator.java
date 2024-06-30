package mod.bespectacled.modernbeta.api.world.spawn;

import net.minecraft.core.BlockPos;

import java.util.Optional;

public interface SpawnLocator {
	Optional<BlockPos> locateSpawn();

	public static final SpawnLocator DEFAULT = new SpawnLocator() {
		@Override
		public Optional<BlockPos> locateSpawn() {
			return Optional.empty();
		}
	};
}
