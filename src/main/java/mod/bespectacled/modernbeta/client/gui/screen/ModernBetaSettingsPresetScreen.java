package mod.bespectacled.modernbeta.client.gui.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import mod.bespectacled.modernbeta.ModernBeta;
import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsPreset;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.CommonColors;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;


public class ModernBetaSettingsPresetScreen extends ModernBetaScreen {
	private static final String TEXT_TITLE = "createWorld.customize.modern_beta.title.preset";
	private static final String TEXT_PRESET_NAME = "createWorld.customize.modern_beta.preset.name";
	private static final String TEXT_PRESET_DESC = "createWorld.customize.modern_beta.preset.desc";
	private static final String TEXT_PRESET_TYPE_DEFAULT = "createWorld.customize.modern_beta.preset.type.default";
	private static final String TEXT_PRESET_TYPE_CUSTOM = "createWorld.customize.modern_beta.preset.type.custom";

	//private static final Identifier TEXTURE_PRESET_DEFAULT = createTextureId("default");
	private static final ResourceLocation TEXTURE_PRESET_CUSTOM = createTextureId("custom");

	private final List<String> presetsDefault;
	private final List<String> presetsCustom;

	private ModernBetaSettingsPreset preset;
	private PresetsListWidget listWidget;
	private Button selectPresetButton;

	public ModernBetaSettingsPresetScreen(
			ModernBetaWorldScreen parent,
			List<String> presetsDefault,
			List<String> presetsCustom,
			ModernBetaSettingsPreset preset
	) {
		super(Component.translatable(TEXT_TITLE), parent);

		this.presetsDefault = presetsDefault;
		this.presetsCustom = presetsCustom;

		this.preset = preset;
	}

	@Override
	protected void init() {
		super.init();

		this.listWidget = new PresetsListWidget(this.presetsDefault, this.presetsCustom);
		this.addWidget(this.listWidget);

		this.selectPresetButton = this.addRenderableWidget(Button.builder(
				Component.translatable("createWorld.customize.presets.select"),
				onPress -> {
					((ModernBetaWorldScreen) this.parent).setPreset(this.preset);
					this.minecraft.setScreen(this.parent);
				}).bounds(this.width / 2 - 155, this.height - 28, 150, 20).build());

		this.addRenderableWidget(Button.builder(
				CommonComponents.GUI_CANCEL,
				button -> this.minecraft.setScreen(this.parent)
		).bounds(this.width / 2 + 5, this.height - 28, 150, 20).build());

		this.updateSelectButton(this.listWidget.getSelected() != null);
	}

	@Override
	public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
		super.render(context, mouseX, mouseY, delta);

		this.listWidget.render(context, mouseX, mouseY, delta);
	}

	@Override
	protected void renderBackgroundWithOverlay(GuiGraphics context) {
	}

	private void updateSelectButton(boolean hasSelected) {
		this.selectPresetButton.active = hasSelected;
	}

	private static ResourceLocation createTextureId(String id) {
		return ModernBeta.createId("textures/gui/preset_" + id + ".png");
	}

	private class PresetsListWidget extends ObjectSelectionList<PresetsListWidget.PresetEntry> {
		private static final int ITEM_HEIGHT = 60;
		private static final int ICON_SIZE = 56;

		public PresetsListWidget(List<String> presetsDefault, List<String> presetsCustom) {
			super(
					ModernBetaSettingsPresetScreen.this.minecraft,
					ModernBetaSettingsPresetScreen.this.width,
					ModernBetaSettingsPresetScreen.this.height - 60,
					32,
					ITEM_HEIGHT
			);

			presetsDefault.forEach(key -> {
				this.addEntry(new PresetEntry(
						key,
						ModernBetaRegistries.SETTINGS_PRESET.get(key),
						false
				));
			});

			presetsCustom.forEach(key -> {
				this.addEntry(new PresetEntry(
						key,
						ModernBetaRegistries.SETTINGS_PRESET_ALT.get(key),
						true
				));
			});
		}

		@Override
		public void setSelected(PresetEntry entry) {
			super.setSelected(entry);

			ModernBetaSettingsPresetScreen.this.updateSelectButton(entry != null);
		}

		@Override
		protected int getScrollbarPosition() {
			return super.getScrollbarPosition() + 30;
		}

		@Override
		public int getRowWidth() {
			return super.getRowWidth() + 85;
		}

		private class PresetEntry extends ObjectSelectionList.Entry<PresetEntry> {
			private static final ResourceLocation TEXTURE_JOIN = ResourceLocation.withDefaultNamespace("world_list/join");
			private static final ResourceLocation TEXTURE_JOIN_HIGHLIGHTED = ResourceLocation.withDefaultNamespace("world_list/join_highlighted");

			private static final int TEXT_SPACING = 11;
			private static final int TEXT_LENGTH = 240;

			private final ResourceLocation presetTexture;
			private final MutableComponent presetType;
			private final MutableComponent presetName;
			private final MutableComponent presetDesc;
			private final ModernBetaSettingsPreset preset;
			private final boolean isCustom;

			private long time;

			public PresetEntry(String presetName, ModernBetaSettingsPreset preset, boolean isCustom) {
				this.presetTexture = isCustom ? TEXTURE_PRESET_CUSTOM : createTextureId(presetName);
				this.presetType = isCustom ? Component.translatable(TEXT_PRESET_TYPE_CUSTOM) : Component.translatable(TEXT_PRESET_TYPE_DEFAULT);
				this.presetName = Component.translatable(TEXT_PRESET_NAME + "." + presetName);
				this.presetDesc = Component.translatable(TEXT_PRESET_DESC + "." + presetName);
				this.preset = preset;
				this.isCustom = isCustom;
			}

			@Override
			public Component getNarration() {
				return Component.empty();
			}

			@Override
			public void render(GuiGraphics context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta) {
				Font textRenderer = ModernBetaSettingsPresetScreen.this.font;

				ChatFormatting presetTypeTextFormatting = this.isCustom ? ChatFormatting.AQUA : ChatFormatting.YELLOW;
				MutableComponent presetTypeText = this.presetType.withStyle(presetTypeTextFormatting, ChatFormatting.ITALIC);
				MutableComponent presetNameText = this.presetName.withStyle(ChatFormatting.WHITE);

				List<FormattedCharSequence> presetDescTexts = this.splitText(textRenderer, this.presetDesc);

				int textStartX = x + ICON_SIZE + 3;
				int textStartY = 1;

				context.drawString(textRenderer, presetNameText, textStartX, y + textStartY, CommonColors.WHITE, false);

				int descSpacing = TEXT_SPACING + textStartY + 1;
				for (FormattedCharSequence line : presetDescTexts) {
					context.drawString(textRenderer, line, textStartX, y + descSpacing, CommonColors.GRAY, false);
					descSpacing += TEXT_SPACING;
				}

				this.draw(context, x, y, this.presetTexture);

				if (ModernBetaSettingsPresetScreen.this.minecraft.options.touchscreen().get().booleanValue() || hovered) {
					boolean isMouseHovering = (mouseX - x) < ICON_SIZE;
					ResourceLocation texture = isMouseHovering ? TEXTURE_JOIN_HIGHLIGHTED : TEXTURE_JOIN;

					context.fill(x, y, x + ICON_SIZE, y + ICON_SIZE, -1601138544);
					context.blitSprite(
							texture,
							x,
							y,
							ICON_SIZE,
							ICON_SIZE
					);
				}
			}

			@Override
			public boolean mouseClicked(double mouseX, double mouseY, int button) {
				if (button != 0) {
					return false;
				}

				this.setPreset();

				if (mouseX - PresetsListWidget.this.getRowLeft() <= ICON_SIZE) {
					this.selectPreset();
				}

				if (Util.getMillis() - this.time < 250L) {
					this.selectPreset();
				}

				this.time = Util.getMillis();

				return true;
			}

			private void setPreset() {
				PresetsListWidget.this.setSelected(this);
				ModernBetaSettingsPresetScreen.this.preset = this.preset.copy();
			}

			private void selectPreset() {
				ModernBetaSettingsPresetScreen presetScreen = ModernBetaSettingsPresetScreen.this;

				presetScreen.minecraft.getSoundManager().play(
						SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0f)
				);

				((ModernBetaWorldScreen) presetScreen.parent).setPreset(this.preset);
				presetScreen.minecraft.setScreen(presetScreen.parent);
			}

			private void draw(GuiGraphics context, int x, int y, ResourceLocation textureId) {
				RenderSystem.enableBlend();
				context.blit(textureId, x, y, 0.0f, 0.0f, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);
				RenderSystem.disableBlend();
			}

			private List<FormattedCharSequence> splitText(Font textRenderer, Component text) {
				return textRenderer.split(text, TEXT_LENGTH);
			}
		}
	}
}
