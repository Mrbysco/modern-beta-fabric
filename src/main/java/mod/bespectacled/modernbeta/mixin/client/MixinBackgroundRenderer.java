package mod.bespectacled.modernbeta.mixin.client;

import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.world.biome.climate.Clime;
import mod.bespectacled.modernbeta.client.color.BlockColorSampler;
import mod.bespectacled.modernbeta.client.world.ModernBetaClientWorld;
import mod.bespectacled.modernbeta.config.ModernBetaConfig;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public abstract class MixinBackgroundRenderer {
	@Unique
	private static Vec3 modernBeta_pos;
	@Unique
	private static int modernBeta_renderDistance = 16;
	@Unique
	private static float modernBeta_fogWeight = calculateFogWeight(16);
	@Unique
	private static boolean modernBeta_isModernBetaWorld = false;

	@ModifyVariable(
			method = "setupColor",
			at = @At(
					value = "INVOKE_ASSIGN",
					target = "Lnet/minecraft/world/level/biome/Biome;getWaterFogColor()I"
			),
			ordinal = 0
	)
	private static int modifyWaterFogColor(int waterFogColor) {
		if (BlockColorSampler.INSTANCE.useWaterColor()) {
			int x = (int) modernBeta_pos.x();
			int z = (int) modernBeta_pos.z();

			Clime clime = BlockColorSampler.INSTANCE.getClimateSampler().get().sample(x, z);
			ModernBeta.LOGGER.error("{}", BlockColorSampler.INSTANCE.colormapUnderwater.getColor(clime.temp(), clime.rain()));
//TODO: TEST WATER FOG COLOR!
			return BlockColorSampler.INSTANCE.colormapUnderwater.getColor(clime.temp(), clime.rain());
		}

		return waterFogColor;
	}

	@Inject(method = "setupColor", at = @At("HEAD"))
	private static void captureVars(Camera camera, float tickDelta, ClientLevel world, int renderDistance, float skyDarkness, CallbackInfo info) {
		modernBeta_pos = camera.getPosition();

		if (modernBeta_renderDistance != renderDistance) {
			modernBeta_renderDistance = renderDistance;
			modernBeta_fogWeight = calculateFogWeight(renderDistance);
		}

		// Track whether current client world is Modern Beta world,
		// old fog weighting won't be used if not.
		modernBeta_isModernBetaWorld = ((ModernBetaClientWorld) world).isModernBetaWorld();
	}

	@ModifyVariable(
			method = "setupColor",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/client/multiplayer/ClientLevel;getSkyColor(Lnet/minecraft/world/phys/Vec3;F)Lnet/minecraft/world/phys/Vec3;"
			),
			index = 7
	)
	private static float modifyFogWeighting(float weight) {
		return modernBeta_isModernBetaWorld && ModernBetaConfig.COMMON.useOldFogColor.get() ? modernBeta_fogWeight : weight;
	}

	@Unique
	private static float calculateFogWeight(int renderDistance) {
		// Old fog formula with old render distance: weight = 1.0F / (float)(4 - renderDistance)
		// where renderDistance is 0-3, 0 being 'Far' and 3 being 'Very Short'

		int clampedDistance = Mth.clamp(renderDistance, 4, 16);
		clampedDistance -= 4;
		clampedDistance /= 4;

		int oldRenderDistance = Math.abs(clampedDistance - 3);

		float weight = 1.0F / (float) (4 - oldRenderDistance);
		weight = 1.0F - (float) Math.pow(weight, 0.25);

		return weight;
	}
}
