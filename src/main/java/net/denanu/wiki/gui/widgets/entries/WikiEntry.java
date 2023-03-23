package net.denanu.wiki.gui.widgets.entries;

import java.util.LinkedList;
import java.util.List;

import org.jetbrains.annotations.Nullable;

import net.denanu.wiki.config.WikiConfig;
import net.denanu.wiki.content.PageContents;
import net.denanu.wiki.gui.WikiScreen;
import net.denanu.wiki.gui.widgets.Icons;
import net.denanu.wiki.gui.widgets.PageListWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class WikiEntry extends AlwaysSelectedEntryListWidget.Entry<WikiEntry> {
	private final MinecraftClient client;
	private boolean showSubPages = false;
	private final float ICON_SCALE = 0.5f;
	private final Identifier page;
	private final PageContents pageContent;
	private PageListWidget parent;
	@Nullable
	private List<WikiEntry> entries;
	private int x = 0;
	private int y = 0;
	private final int indent;
	private final Type type;

	public WikiEntry(final Identifier page, final PageListWidget parent, final int indnet, final Type type) {
		this.client = MinecraftClient.getInstance();
		this.page = page;
		this.pageContent = PageContents.fromJson(page);
		this.parent = parent;
		this.indent = indnet;
		this.type = type;
	}

	public WikiEntry(final Identifier page, final Type type) {
		this(page, null, 0, type);
	}

	public WikiEntry(final String id, final PageListWidget parent, final int indent, final Type type) {
		this(new Identifier(id), parent, indent, type);
	}

	public WikiEntry(final String id) {
		this(new Identifier(id), null, 0, Type.NORMAL);
	}

	public void setParent(final PageListWidget parent) {
		this.parent = parent;
	}

	@Override
	public Text getNarration() {
		return Text.literal("hi");
	}

	public PageContents getContent() {
		return this.pageContent;
	}

	@Override
	public void render(final MatrixStack stack, final int index, final int y, int x, final int rowWidth, final int rowHeight, final int mouseX, final int mouseY, final boolean hovered, final float delta) {
		if (this.type == Type.NORMAL) {
			x += this.indent;
			if (this.pageContent.getSubPages().size() > 0) {
				this.renderSubPageShowButton(stack, y, x, mouseX, mouseY, hovered);
			}
			for (int idx = 0; idx < this.indent; idx += 5) {
				WikiScreen.drawVerticalLine(stack, x + 2 + idx, y-3, y+14, 0x55FFFFFF);
			}
		}
		this.renderTitle(stack, x + 16, y + 2);

		this.x = x;
		this.y = y;
	}

	private void renderTitle(final MatrixStack stack, final int x, final int y) {
		this.client.textRenderer.draw(stack, Text.translatable(this.page.toTranslationKey("wiki")), x, y, WikiConfig.getColor());

	}

	private void renderSubPageShowButton(final MatrixStack stack, final int y, final int x, final int mouseX, final int mouseY, final boolean hovered) {
		this.getSubPageIcon().render(stack, x, y-2, mouseX, mouseY, hovered, this.ICON_SCALE);
	}

	private Icons getSubPageIcon() {
		if (this.showSubPages) {
			return Icons.SUB_PAGES_SHOWN;
		}
		return Icons.SUB_PAGES_CLOSED;
	}

	@Override
	public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
		if (this.getSubPageIcon().isOver(this.x, this.y, (int)mouseX, (int)mouseY, this.ICON_SCALE) && button == 0) {
			this.showSubPages = !this.showSubPages;
			this.parent.updatePageList(this);
			return false;
		}
		return true;
	}

	private WikiEntry of(final String id) {
		return new WikiEntry(id, this.parent, this.indent + 5, Type.NORMAL);
	}

	private void loadEntriesIfNeeded() {
		if (this.entries == null) {
			this.entries = this.pageContent.getSubPages().stream().map(this::of).toList();
		}
	}

	public void addChildren(final LinkedList<WikiEntry> children) {
		if (this.showSubPages) {
			this.loadEntriesIfNeeded();
			for (final WikiEntry entry : this.entries) {
				children.add(entry);
				entry.addChildren(children);
			}
		}
		else {
			this.entries = null;
		}
	}

	public enum Type {
		NORMAL,
		FILTERED;
	}
}
