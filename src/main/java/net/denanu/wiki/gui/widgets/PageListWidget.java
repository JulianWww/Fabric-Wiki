package net.denanu.wiki.gui.widgets;

import net.denanu.wiki.gui.widgets.entries.WikiEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.util.math.MatrixStack;

public class PageListWidget extends AlwaysSelectedEntryListWidget<WikiEntry> implements AutoCloseable {

	public PageListWidget(final MinecraftClient minecraftClient, final int width, final int height, final int top, final int bottom, final int itemHeight) {
		super(minecraftClient, width, height, top, bottom, itemHeight);
		this.update();
	}

	private void update() {
		for (int i = 0; i < 64; i++) {
			this.addEntry(new WikiEntry());
		}
	}

	@Override
	public int getRowWidth() {
		return this.width;
	}

	@Override
	protected int getScrollbarPositionX() {
		return this.width;
	}

	@Override
	public void render(final MatrixStack stack, final int mouseX, final int mouseY, final float delta) {
		super.render(stack, mouseX, mouseY, delta);
	}

	@Override
	public void close() throws Exception {
	}
}
