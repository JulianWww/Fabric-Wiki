package net.denanu.wiki.gui.widgets.entries;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

public class WikiEntry extends AlwaysSelectedEntryListWidget.Entry<WikiEntry> {
	private final MinecraftClient client;

	public WikiEntry() {
		this.client = MinecraftClient.getInstance();
	}

	@Override
	public Text getNarration() {
		return Text.literal("hi");
	}

	@Override
	public void render(final MatrixStack matrices, final int index, final int y, final int x, final int rowWidth, final int rowHeight, final int mouseX, final int mouseY, final boolean hovered, final float delta) {
		final TextRenderer font = this.client.textRenderer;
		font.draw(matrices, "hi", x, y, 16777215);
	}
}
