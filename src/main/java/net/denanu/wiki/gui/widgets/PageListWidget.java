package net.denanu.wiki.gui.widgets;

import net.denanu.wiki.gui.widgets.entries.WikiEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;

public class PageListWidget extends AlwaysSelectedEntryListWidget<WikiEntry> implements AutoCloseable {

	public PageListWidget(final MinecraftClient minecraftClient, final int width, final int height, final int top, final int bottom, final int itemHeight) {
		super(minecraftClient, width, height, top, bottom, itemHeight);
		this.update();
	}

	private void update() {
		this.addEntry(new WikiEntry());
	}

	@Override
	public void close() throws Exception {
	}
}
