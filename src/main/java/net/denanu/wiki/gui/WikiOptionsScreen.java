package net.denanu.wiki.gui;

import net.denanu.wiki.config.WikiConfig;
import net.denanu.wiki.config.WikiConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class WikiOptionsScreen extends GameOptionsScreen {

	private final Screen previous;
	private OptionListWidget list;

	@SuppressWarnings("resource")
	public WikiOptionsScreen(final Screen previous) {
		super(previous, MinecraftClient.getInstance().options, Text.translatable("wiki.options"));
		this.previous = previous;
	}


	@Override
	protected void init() {
		this.list = new OptionListWidget(this.client, this.width, this.height, 32, this.height - 32, 25);
		this.list.addAll(WikiConfig.asOptions());
		this.addSelectableChild(this.list);
		this.addDrawableChild(
				ButtonWidget.builder(ScreenTexts.DONE, button -> {
					WikiConfigManager.save();
					this.client.setScreen(this.previous);
				}).position(this.width / 2 - 100, this.height - 27)
				.size(200, 20)
				.build());
	}

	@Override
	public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
		this.renderBackground(matrices);
		this.list.render(matrices, mouseX, mouseY, delta);
		DrawableHelper.drawCenteredTextWithShadow(matrices, this.textRenderer, this.title, this.width / 2, 5, 0xffffff);
		super.render(matrices, mouseX, mouseY, delta);
	}

	@Override
	public void removed() {
		WikiConfigManager.save();
	}
}
