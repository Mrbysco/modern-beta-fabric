package mod.bespectacled.modernbeta.client.gui.screen;

import mod.bespectacled.modernbeta.ModernBetaBuiltInTypes;
import mod.bespectacled.modernbeta.api.registry.ModernBetaRegistries;
import mod.bespectacled.modernbeta.settings.ModernBetaSettingsPreset;
import mod.bespectacled.modernbeta.world.biome.ModernBetaBiomeSource;
import mod.bespectacled.modernbeta.world.chunk.ModernBetaChunkGenerator;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.FrameLayout;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.worldselection.WorldCreationContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Tuple;
import net.minecraft.world.level.chunk.ChunkGenerator;
import org.apache.logging.log4j.util.TriConsumer;

import java.util.Random;

public class ModernBetaWorldScreen extends ModernBetaScreen {
	private static final String TEXT_TITLE = "createWorld.customize.modern_beta.title";
	private static final String TEXT_TITLE_CHUNK = "createWorld.customize.modern_beta.title.chunk";
	private static final String TEXT_TITLE_BIOME = "createWorld.customize.modern_beta.title.biome";
	private static final String TEXT_TITLE_CAVE_BIOME = "createWorld.customize.modern_beta.title.cave_biome";

	private static final String TEXT_PRESET = "createWorld.customize.modern_beta.preset";
	private static final String TEXT_PRESET_NAME = "createWorld.customize.modern_beta.preset.name";
	private static final String TEXT_PRESET_CUSTOM = "createWorld.customize.modern_beta.preset.custom";

	private static final String TEXT_CHUNK = "createWorld.customize.modern_beta.chunk";
	private static final String TEXT_BIOME = "createWorld.customize.modern_beta.biome";
	private static final String TEXT_CAVE_BIOME = "createWorld.customize.modern_beta.cave_biome";

	private static final String TEXT_SETTINGS = "createWorld.customize.modern_beta.settings";
	private static final String TEXT_SETTINGS_RESET = "createWorld.customize.modern_beta.settings.reset";
	private static final String TEXT_SETTINGS_RESET_MESSAGE = "createWorld.customize.modern_beta.settings.reset.message";
	//private static final String TEXT_INVALID_SETTINGS = "createWorld.customize.modern_beta.invalid_settings";

	private static final String[] TEXT_HINTS = new String[]{
			"createWorld.customize.modern_beta.hint.settings"
	};

	private final TriConsumer<CompoundTag, CompoundTag, CompoundTag> onDone;
	private final String hintString;

	private ModernBetaSettingsPreset preset;
	private Button buttonPreset;

	public ModernBetaWorldScreen(Screen parent, WorldCreationContext generatorOptionsHolder, TriConsumer<CompoundTag, CompoundTag, CompoundTag> onDone) {
		super(Component.translatable(TEXT_TITLE), parent);

		ChunkGenerator chunkGenerator = generatorOptionsHolder.selectedDimensions().overworld();
		ModernBetaChunkGenerator modernBetaChunkGenerator = (ModernBetaChunkGenerator) chunkGenerator;
		ModernBetaBiomeSource modernBetaBiomeSource = (ModernBetaBiomeSource) modernBetaChunkGenerator.getBiomeSource();

		this.onDone = onDone;
		this.hintString = TEXT_HINTS[new Random().nextInt(TEXT_HINTS.length)];

		this.preset = new ModernBetaSettingsPreset(
				modernBetaChunkGenerator.getChunkSettings(),
				modernBetaBiomeSource.getBiomeSettings(),
				modernBetaBiomeSource.getCaveBiomeSettings()
		);
	}

	public void setPreset(ModernBetaSettingsPreset preset) {
		this.preset = preset;
	}

	@Override
	protected void init() {
		super.init();

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_DONE, button -> {
			this.onDone.accept(
					this.preset.settingsChunk().toCompound(),
					this.preset.settingsBiome().toCompound(),
					this.preset.settingsCaveBiome().toCompound()
			);
			this.minecraft.setScreen(this.parent);
		}).bounds(this.width / 2 - 155, this.height - 28, BUTTON_LENGTH, BUTTON_HEIGHT).build());

		this.addRenderableWidget(Button.builder(CommonComponents.GUI_CANCEL, button ->
				this.minecraft.setScreen(this.parent)
		).bounds(this.width / 2 + 5, this.height - 28, BUTTON_LENGTH, BUTTON_HEIGHT).build());

		Component hintText = Component.translatable(this.hintString).withStyle(ChatFormatting.GRAY);
		int hintTextWidth = this.font.width(hintText.getVisualOrderText());
		int hintTextHeight = this.font.lineHeight;

		this.addRenderableWidget(new StringWidget(
				this.width / 2 - hintTextWidth / 2,
				this.height - 46,
				hintTextWidth,
				hintTextHeight,
				hintText,
				this.font
		));

		ChatFormatting presetTextColor = ModernBetaRegistries.SETTINGS_PRESET.contains(this.getPresetKey()) ?
				ChatFormatting.YELLOW :
				ChatFormatting.AQUA;
		MutableComponent presetText = Component.translatable(TEXT_PRESET).append(": ");
		presetText.append(this.isPresetCustom() ?
				Component.translatable(TEXT_PRESET_CUSTOM) :
				Component.translatable(TEXT_PRESET_NAME + "." + this.getPresetKey()).withStyle(presetTextColor)
		);

		this.buttonPreset = Button.builder(
				presetText,
				button -> this.minecraft.setScreen(new ModernBetaSettingsPresetScreen(
						this,
						ModernBetaRegistries.SETTINGS_PRESET.getKeySet().stream().toList(),
						ModernBetaRegistries.SETTINGS_PRESET_ALT.getKeySet().stream().toList(),
						this.preset
				))
		).bounds(0, 0, BUTTON_LENGTH_PRESET, BUTTON_HEIGHT_PRESET).build();

		Button buttonChunk = Button.builder(
				Component.translatable(TEXT_SETTINGS),
				button -> this.minecraft.setScreen(new ModernBetaSettingsScreen(
						TEXT_TITLE_CHUNK,
						this,
						this.preset.settingsChunk(),
						string -> {
							Tuple<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.set(string, "", "");

							this.preset = updatedPreset.getA();
						}
				))
		).build();

		Button buttonBiome = Button.builder(
				Component.translatable(TEXT_SETTINGS),
				button -> this.minecraft.setScreen(new ModernBetaSettingsScreen(
						TEXT_TITLE_BIOME,
						this,
						this.preset.settingsBiome(),
						string -> {
							Tuple<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.set("", string, "");

							this.preset = updatedPreset.getA();
						}
				))
		).build();

		Button buttonCaveBiome = Button.builder(
				Component.translatable(TEXT_SETTINGS),
				button -> this.minecraft.setScreen(new ModernBetaSettingsScreen(
						TEXT_TITLE_CAVE_BIOME,
						this,
						this.preset.settingsCaveBiome(),
						string -> {
							Tuple<ModernBetaSettingsPreset, Boolean> updatedPreset = this.preset.set("", "", string);

							this.preset = updatedPreset.getA();
						}
				))
		).build();

		Button buttonReset = Button.builder(
				Component.translatable(TEXT_SETTINGS_RESET),
				button -> this.minecraft.setScreen(new ModernBetaSettingsConfirmScreen(
						this,
						this::resetPreset,
						Component.translatable(TEXT_SETTINGS_RESET_MESSAGE),
						Component.translatable(TEXT_SETTINGS_RESET)
				))
		).build();

		GridLayout gridWidgetMain = this.createGridWidget();
		GridLayout gridWidgetSettings = this.createGridWidget();

		GridLayout.RowHelper gridAdderMain = gridWidgetMain.createRowHelper(1);
		GridLayout.RowHelper gridAdderSettings = gridWidgetSettings.createRowHelper(2);
		gridAdderSettings.defaultCellSetting().alignVerticallyMiddle();

		gridAdderMain.addChild(this.buttonPreset);
		gridAdderMain.addChild(gridWidgetSettings);
		gridAdderMain.addChild(buttonReset);

		this.addGridTextButtonPair(gridAdderSettings, TEXT_CHUNK, buttonChunk);
		this.addGridTextButtonPair(gridAdderSettings, TEXT_BIOME, buttonBiome);
		this.addGridTextButtonPair(gridAdderSettings, TEXT_CAVE_BIOME, buttonCaveBiome);

		gridWidgetMain.arrangeElements();
		FrameLayout.alignInRectangle(gridWidgetMain, 0, this.overlayTop + 8, this.width, this.height, 0.5f, 0.0f);
		gridWidgetMain.visitWidgets(this::addRenderableWidget);

		this.onPresetChange();
	}

	private void onPresetChange() {
		if (this.isPresetCustom()) {
			this.buttonPreset.active = false;
		} else {
			this.buttonPreset.active = true;
		}
	}

	private void resetPreset() {
		this.preset = ModernBetaRegistries.SETTINGS_PRESET.get(ModernBetaBuiltInTypes.Preset.BETA.id);
		this.onPresetChange();
	}

	private boolean isPresetCustom() {
		return !(ModernBetaRegistries.SETTINGS_PRESET.contains(this.preset) ||
				ModernBetaRegistries.SETTINGS_PRESET_ALT.contains(this.preset));
	}

	private String getPresetKey() {
		if (ModernBetaRegistries.SETTINGS_PRESET.contains(this.preset))
			return ModernBetaRegistries.SETTINGS_PRESET.getKey(this.preset);

		if (ModernBetaRegistries.SETTINGS_PRESET_ALT.contains(this.preset))
			return ModernBetaRegistries.SETTINGS_PRESET_ALT.getKey(this.preset);

		return null;
	}
}
