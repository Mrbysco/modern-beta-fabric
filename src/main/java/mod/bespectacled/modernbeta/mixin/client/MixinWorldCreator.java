package mod.bespectacled.modernbeta.mixin.client;

import mod.bespectacled.modernbeta.client.gui.screen.ModernBetaWorldScreen;
import mod.bespectacled.modernbeta.client.gui.screen.ModernBetaWorldScreenProvider;
import mod.bespectacled.modernbeta.world.preset.ModernBetaWorldPresets;
import net.minecraft.client.gui.screens.worldselection.PresetEditor;
import net.minecraft.client.gui.screens.worldselection.WorldCreationUiState;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.presets.WorldPreset;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(WorldCreationUiState.class)
public abstract class MixinWorldCreator {
	@Inject(method = "getPresetEditor", at = @At("RETURN"), cancellable = true)
	public void injectGetLevelScreenProvider(CallbackInfoReturnable<PresetEditor> info) {
		Holder<WorldPreset> preset = this.getWorldType().preset();
		ResourceKey<WorldPreset> modernBeta = ModernBetaWorldPresets.MODERN_BETA;

		if (preset != null && preset.unwrapKey().isPresent() && preset.unwrapKey().get().equals(modernBeta)) {
			info.setReturnValue(
					(parent, generatorOptionsHolder) -> {
						return new ModernBetaWorldScreen(
								parent,
								generatorOptionsHolder,
								(settingsChunk, settingsBiome, settingsCaveBiome) -> parent.getUiState().updateDimensions(
										ModernBetaWorldScreenProvider.createModifier(
												settingsChunk,
												settingsBiome,
												settingsCaveBiome
										)
								)
						);
					}
			);
		}
	}

	@Shadow
	public abstract WorldCreationUiState.WorldTypeEntry getWorldType();
}
