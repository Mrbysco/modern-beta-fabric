package mod.bespectacled.modernbeta.mixin.client;

import mod.bespectacled.modernbeta.api.world.biome.climate.Clime;
import mod.bespectacled.modernbeta.client.color.BlockColorSampler;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockAndTintGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(BiomeColors.class)
public abstract class MixinBiomeColors {
	@Inject(method = "getAverageWaterColor", at = @At("HEAD"), cancellable = true)
	private static void injectGetAverageWaterColor(BlockAndTintGetter world, BlockPos pos, CallbackInfoReturnable<Integer> info) {
		if (BlockColorSampler.INSTANCE.useWaterColor()) {
			Clime clime = BlockColorSampler.INSTANCE.getClimateSampler().get().sample(pos.getX(), pos.getZ());

			info.setReturnValue(BlockColorSampler.INSTANCE.colormapWater.getColor(clime.temp(), clime.rain()));
		}
	}
}
