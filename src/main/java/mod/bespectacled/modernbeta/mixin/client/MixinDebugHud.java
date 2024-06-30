package mod.bespectacled.modernbeta.mixin.client;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.world.biome.climate.ClimateSampler;
import mod.bespectacled.modernbeta.api.world.biome.climate.Clime;
import mod.bespectacled.modernbeta.api.world.cavebiome.climate.CaveClimateSampler;
import mod.bespectacled.modernbeta.api.world.cavebiome.climate.CaveClime;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProvider;
import mod.bespectacled.modernbeta.api.world.chunk.ChunkProviderNoise;
import mod.bespectacled.modernbeta.util.chunk.ChunkHeightmap;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.biome.injector.BiomeInjector.BiomeInjectionStep;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;


@Mixin(DebugScreenOverlay.class)
public abstract class MixinDebugHud {
	@Shadow
	private Minecraft minecraft;

	@Inject(method = "getGameInformation", at = @At("TAIL"))
	private void injectGetGameInformation(CallbackInfoReturnable<List<String>> info) {
		BlockPos pos = this.minecraft.getCameraEntity().blockPosition();
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();

		IntegratedServer integratedServer = this.minecraft.getSingleplayerServer();
		ServerLevel serverWorld = null;

		if (integratedServer != null) {
			serverWorld = integratedServer.getLevel(this.minecraft.level.dimension());
		}

		if (serverWorld != null && ModernBeta.DEV_ENV) {
			ChunkGenerator chunkGenerator = serverWorld.getChunkSource().getGenerator();
			BiomeSource biomeSource = chunkGenerator.getBiomeSource();

			if (biomeSource instanceof ModernBetaBiomeSource modernBetaBiomeSource) {
				if (modernBetaBiomeSource.getBiomeProvider() instanceof ClimateSampler climateSampler) {
					Clime clime = climateSampler.sample(x, z);
					double temp = clime.temp();
					double rain = clime.rain();

					info.getReturnValue().add(
							String.format(
									"[Modern Beta] Climate Temp: %.3f Rainfall: %.3f",
									temp,
									rain
							)
					);
				}

				if (modernBetaBiomeSource.getCaveBiomeProvider() instanceof CaveClimateSampler climateSampler) {
					CaveClime clime = climateSampler.sample(x >> 2, y >> 2, z >> 2);
					double temp = clime.temp();
					double rain = clime.rain();

					info.getReturnValue().add(
							String.format(
									"[Modern Beta] Cave Climate Temp: %.3f Rainfall: %.3f",
									temp,
									rain
							)
					);
				}

			}

			if (chunkGenerator instanceof ModernBetaChunkGenerator modernBetaChunkGenerator) {
				ChunkProvider chunkProvider = modernBetaChunkGenerator.getChunkProvider();

				info.getReturnValue().add(
						String.format(
								"[Modern Beta] Chunk Provider WS height: %d OF height: %d Sea level: %d",
								chunkProvider.getHeight(x, z, Types.WORLD_SURFACE_WG),
								chunkProvider.getHeight(x, z, Types.OCEAN_FLOOR),
								chunkProvider.getSeaLevel()
						)
				);

				if (chunkProvider instanceof ChunkProviderNoise noiseChunkProvider) {
					info.getReturnValue().add(
							String.format(
									"[Modern Beta] Noise Chunk Provider WSF height: %d",
									noiseChunkProvider.getHeight(x, z, ChunkHeightmap.Type.SURFACE_FLOOR)
							)
					);
				}

                /*
                int worldMinY = modernBetaChunkGenerator.getMinimumY();
                int minHeight = modernBetaChunkGenerator.getBiomeInjector().sampleMinHeightAround(biomeX, biomeZ);
                BiomeInjectionContext context = new BiomeInjectionContext(worldMinY, -1, minHeight).setY(y);
                
                boolean canPlaceCave = BiomeInjector.CAVE_PREDICATE.test(context);
                
                info.getReturnValue().add(
                    String.format(
                        "[Modern Beta] Valid cave biome position: %b",
                        canPlaceCave
                    )
                );
                */

				if (modernBetaChunkGenerator.getBiomeInjector() != null) {
					Holder<Biome> biome = modernBetaChunkGenerator.getBiomeInjector().getBiomeAtBlock(x, y, z, null, BiomeInjectionStep.ALL);
					info.getReturnValue().add(
							String.format(
									"[Modern Beta] Injected biome: %s",
									biome.unwrapKey().get().location().toString()
							)
					);
				}
			}
		}
	}
}
