package mod.bespectacled.modernbeta.mixin;

import mod.bespectacled.modernbeta.api.world.spawn.SpawnLocator;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.PlayerRespawnLogic;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRespawnLogic.class)
public abstract class MixinSpawnLocating {
	/*
	 * Override vanilla behavior of moving player to highest solid block,
	 * even after finding initial spawn point.
	 */
	@Inject(method = "getOverworldRespawnPos", at = @At("HEAD"), cancellable = true)
	private static void injectGetOverworldRespawnPos(ServerLevel world, int x, int z, CallbackInfoReturnable<BlockPos> info) {
		ChunkGenerator chunkGenerator = world.getChunkSource().getGenerator();

		if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator &&
				modernBetaChunkGenerator.getChunkProvider().getSpawnLocator() != SpawnLocator.DEFAULT
		) {
			int spawnY = world.getLevelData().getSpawnPos().getY();

			info.setReturnValue(new BlockPos(x, spawnY, z));
		}
	}
}
