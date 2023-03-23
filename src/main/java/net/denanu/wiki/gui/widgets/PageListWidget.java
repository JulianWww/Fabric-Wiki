package net.denanu.wiki.gui.widgets;

import java.util.LinkedList;

import net.denanu.wiki.gui.WikiScreen;
import net.denanu.wiki.gui.widgets.entries.WikiEntry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.util.math.MatrixStack;

public class PageListWidget extends AlwaysSelectedEntryListWidget<WikiEntry> implements AutoCloseable {
	private final WikiScreen screen;

	public PageListWidget(final MinecraftClient minecraftClient, final int width, final int height, final int top, final int bottom, final int itemHeight, final WikiScreen screen) {
		super(minecraftClient, width, height, top, bottom, itemHeight);
		this.replaceEntries(screen.root.getChildren());
		this.screen = screen;

		screen.root.getChildren().stream().forEach(child -> child.setParent(this));
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

	@Override
	public void setSelected(final WikiEntry entry) {
		super.setSelected(entry);
		this.screen.contents.setContents(entry.getContent());
	}

	public void updatePageList(final WikiEntry updator) {
		final LinkedList<WikiEntry> children = new LinkedList<>();
		final WikiEntry selected = this.getSelectedOrNull();

		for (final WikiEntry topChildren : this.screen.root.getChildren()) {
			children.add(topChildren);
			topChildren.addChildren(children);
		}

		this.replaceEntries(children);
		if (children.contains(selected)) {
			this.setSelected(selected);
		} else {
			this.setSelected(updator);
		}
	}
}
