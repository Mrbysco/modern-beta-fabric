package mod.bespectacled.modernbeta.mixin;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProvider;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProviderFinite;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import mod.bespectacled.modernbeta.world.chunk.provider.ChunkProviderIndev;
import mod.bespectacled.modernbeta.world.chunk.provider.indev.IndevTheme;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.PlayerRespawnLogic;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.storage.ServerLevelData;
import org.slf4j.event.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public abstract class MixinMinecraftServer {
	@Inject(method = "setInitialSpawn", at = @At("RETURN"))
	private static void injectSetInitialSpawn(ServerLevel world, ServerLevelData worldProperties, boolean bonusChest, boolean debugWorld, CallbackInfo info) {
		ChunkGenerator chunkGenerator = world.getChunkSource().getGenerator();

		// Set old spawn angle (doesn't seem to work?)
		if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
			worldProperties.setSpawn(worldProperties.getSpawnPos(), -90.0f);
		}
	}

	@Redirect(
			method = "setInitialSpawn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/PlayerRespawnLogic;getSpawnPosInChunk(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/ChunkPos;)Lnet/minecraft/core/BlockPos;"
			)
	)
	private static BlockPos redirectSetInitialSpawn(ServerLevel world, ChunkPos chunkPos) {
		ChunkGenerator chunkGenerator = world.getChunkSource().getGenerator();
		BlockPos spawnPos = PlayerRespawnLogic.getSpawnPosInChunk(world, chunkPos);

		if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
			ChunkProvider chunkProvider = modernBetaChunkGenerator.getChunkProvider();

			world.getGameRules().getRule(GameRules.RULE_SPAWN_RADIUS).set(0, world.getServer()); // Ensure a centered spawn
			spawnPos = chunkProvider.getSpawnLocator().locateSpawn().orElse(spawnPos);

			if (spawnPos != null && ModernBeta.DEV_ENV) {
				int x = spawnPos.getX();
				int y = spawnPos.getY();
				int z = spawnPos.getZ();

				ModernBeta.log(Level.INFO, String.format("Spawning at %d/%d/%d", x, y, z));
			}

			if (spawnPos != null && chunkProvider instanceof ChunkProviderIndev chunkProviderIndev) {
				// Generate Indev house
				chunkProviderIndev.generateIndevHouse(world, spawnPos);

				// Set Indev world properties.
				setIndevProperties(world, chunkProviderIndev.getLevelTheme());
			}

			if (chunkProvider instanceof ChunkProviderFinite chunkProviderFinite) {
				ChunkProviderFinite.resetPhase();
			}
		}

		return spawnPos;
	}

	@Unique
	private static void setIndevProperties(ServerLevel world, IndevTheme theme) {
		switch (theme) {
			case HELL -> {
				world.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, null);
				world.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(false, null);
				world.setDayTime(18000);
			}
			case PARADISE -> {
				world.getGameRules().getRule(GameRules.RULE_DAYLIGHT).set(false, null);
				world.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(false, null);
				world.setDayTime(6000);
			}
			case WOODS -> {
				world.getGameRules().getRule(GameRules.RULE_WEATHER_CYCLE).set(false, null);
				world.setWeatherParameters(0, Integer.MAX_VALUE, true, false);
			}
			default -> {
			}
		}
	}
}
