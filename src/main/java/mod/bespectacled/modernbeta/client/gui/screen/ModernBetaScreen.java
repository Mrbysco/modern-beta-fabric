package mod.bespectacled.modernbeta.client.gui.screen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class ModernBetaScreen extends Screen {
	public static final int BUTTON_HEIGHT = 20;
	public static final int BUTTON_LENGTH = 150;
	public static final int BUTTON_LENGTH_PRESET = 200;
	public static final int BUTTON_HEIGHT_PRESET = 20;

	public static final int TEXTURE_HEIGHT = 50;
	public static final int TEXTURE_LENGTH = 150;

	protected final Screen parent;
	protected int overlayLeft;
	protected int overlayRight;
	protected int overlayTop;
	protected int overlayBottom;

	public ModernBetaScreen(Component title, Screen parent) {
		super(title);

		this.parent = parent;
	}

	@Override
	public void onClose() {
		this.minecraft.setScreen(this.parent);
	}

	@Override
	public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
		this.renderBackgroundWithOverlay(context);

		context.drawCenteredString(this.font, this.title, this.width / 2, 16, 0xFFFFFF);

		super.render(context, mouseX, mouseY, delta);
	}

	@Override
	protected void init() {
		this.overlayLeft = 0;
		this.overlayRight = this.width;
		this.overlayTop = 32;
		this.overlayBottom = this.height - 32;
	}

	protected GridLayout createGridWidget() {
		GridLayout gridWidget = new GridLayout();
		gridWidget.defaultCellSetting().paddingHorizontal(5).paddingBottom(4).alignHorizontallyCenter().alignVerticallyTop();

		return gridWidget;
	}

	protected void addGridTextButtonPair(GridLayout.RowHelper adder, String text, Button buttonWidget) {
		adder.addChild(new StringWidget(Component.translatable(text), this.font));
		adder.addChild(buttonWidget);
	}

	protected void renderBackgroundOverlay(GuiGraphics context) {
		context.setColor(0.125f, 0.125f, 0.125f, 1.0f);
		context.blit(Screen.MENU_BACKGROUND, this.overlayLeft, this.overlayTop, this.overlayRight, this.overlayBottom, this.overlayRight - this.overlayLeft, this.overlayBottom - this.overlayTop, 32, 32);
		context.setColor(1.0f, 1.0f, 1.0f, 1.0f);
	}

	protected void renderBackgroundGradient(GuiGraphics context) {
		context.setColor(0.25f, 0.25f, 0.25f, 1.0f);
		context.blit(Screen.MENU_BACKGROUND, this.overlayLeft, 0, -100, 0.0f, 0.0f, this.width, this.overlayTop, 32, 32);
		context.blit(Screen.MENU_BACKGROUND, this.overlayLeft, this.overlayBottom, -100, 0.0f, this.overlayBottom, this.width, this.height - this.overlayBottom, 32, 32);
		context.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		context.fillGradient(this.overlayLeft, this.overlayTop, this.overlayRight, this.overlayTop + 4, -16777216, 0);
		context.fillGradient(this.overlayLeft, this.overlayBottom - 4, this.overlayRight, this.overlayBottom, 0, -16777216);
	}

	protected void renderBackgroundWithOverlay(GuiGraphics context) {
//		this.renderDirtBackground(context); TODO: Check the ModernBetaScreen to see if this is correct
		this.renderBackgroundOverlay(context);
		this.renderBackgroundGradient(context);
	}
}
