package net.denanu.wiki.gui;

import org.joml.Matrix4f;

import com.mojang.blaze3d.systems.RenderSystem;
import com.terraformersmc.modmenu.gui.ModsScreen;

import net.denanu.wiki.content.RootData;
import net.denanu.wiki.gui.widgets.Icons;
import net.denanu.wiki.gui.widgets.PageContentWidget;
import net.denanu.wiki.gui.widgets.PageListWidget;
import net.denanu.wiki.gui.widgets.buttons.TexturedButtonWidgetNoFocus;
import net.denanu.wiki.search.SearchManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.ColorHelper;

@Environment(value=EnvType.CLIENT)
public class WikiScreen extends Screen {
	public final RootData root;
	public PageContentWidget contents;
	private PageListWidget pageList;
	protected final Screen parent;
	private TextFieldWidget searchBox;
	public final SearchManager searcher;

	protected WikiScreen(final Screen parent, final RootData root, final Identifier rootId) {
		super(root.getTitle());
		this.root = root;
		this.parent = parent;
		this.searcher = new SearchManager(rootId);
	}

	public static WikiScreen of(final Identifier root, final ModsScreen screen) {
		final RootData rootData = RootData.fromJson(root);
		return new WikiScreen(screen, rootData, root);
	}

	@Override
	public void init() {
		this.contents = new PageContentWidget(
				this.getContentTopX(),
				this.getContentTopY(),
				this.getContentWidth(),
				this.getContentHeight(),
				this.root, this.itemRenderer, this
				);
		this.pageList = new PageListWidget(this.client, this.getContentTopX() - 4, this.getContentHeight(), this.getContentTopY(), this.getContentTopY() + this.getContentHeight() - 22, 16, this, this.pageList);
		this.pageList.setLeftPos(0);

		this.addDrawableChild(this.contents);
		this.addDrawableChild(this.pageList);

		this.addHomeButton();
		this.addSearchBar();
	}

	private void addSearchBar() {
		this.searchBox = new TextFieldWidget(this.textRenderer, 2, 2, this.leftWidth() - 3, 20, this.searchBox, Text.translatable("modmenu.search"));
		this.searchBox.setChangedListener(str -> this.pageList.fillterPageList(str));
		this.addSelectableChild(this.searchBox);
	}

	@Override
	public void tick() {
		super.tick();
		this.searchBox.tick();
	}

	@Override
	public boolean charTyped(final char chr, final int keyCode) {
		return this.searchBox.charTyped(chr, keyCode);
	}

	private void addHomeButton() {
		final int y = this.getContentTopY() + this.getContentHeight() - 20;
		final TexturedButtonWidgetNoFocus homeButton = new TexturedButtonWidgetNoFocus(2, y, 20, 20, 0, 0, 20, Icons.SMALL_BUTTONS, 20, 40, button -> {
			this.contents.setContents(this.root);
			this.pageList.setSelected(null);
		});
		homeButton.setTooltip(Tooltip.of(Text.translatable("wiki.home")));
		this.addDrawableChild(homeButton);

		this.addDrawableChild(
				ButtonWidget.builder(Text.translatable("category.modmenu.name"), b -> {this.close();})
				.position(24, y)
				.size(this.getContentTopX() - 28, 20)
				.tooltip(Tooltip.of(Text.translatable("wiki.return_to_modmenu")))
				.build()
				);
	}

	public int leftWidth() {
		return this.getContentTopX() - 4;
	}

	public int getContentTopX() {
		return this.width/3;
	}
	public int getContentTopY() {
		return 24;
	}
	public int getContentWidth() {
		return this.width - this.getContentTopX() - 10;
	}
	public int getContentHeight() {
		return this.height - this.getContentTopY() - 10;
	}

	@Override
	public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
		this.renderBackground(matrices);
		super.render(matrices, mouseX, mouseY, delta);
		this.searchBox.render(matrices, mouseX, mouseY, delta);

		matrices.push();
		matrices.scale(2f, 2f, 1f);
		DrawableHelper.drawCenteredTextWithShadow(matrices, this.textRenderer, this.title, this.width / 4, 2, 16777215);
		matrices.pop();
	}

	@Override
	public void renderBackground(final MatrixStack matrices) {
		WikiScreen.overlayBackground(0, 0, this.width, this.height, 64, 64, 64, 255, DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
	}

	static void overlayBackground(final int x1, final int y1, final int x2, final int y2, final int red, final int green, final int blue, final int alpha, final Identifier texture) {
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder buffer = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		RenderSystem.setShaderTexture(0, texture);
		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
		buffer.vertex(x1, y2, 0.0D).texture(x1 / 32.0F, y2 / 32.0F).color(red, green, blue, alpha).next();
		buffer.vertex(x2, y2, 0.0D).texture(x2 / 32.0F, y2 / 32.0F).color(red, green, blue, alpha).next();
		buffer.vertex(x2, y1, 0.0D).texture(x2 / 32.0F, y1 / 32.0F).color(red, green, blue, alpha).next();
		buffer.vertex(x1, y1, 0.0D).texture(x1 / 32.0F, y1 / 32.0F).color(red, green, blue, alpha).next();
		tessellator.draw();
	}

	@Override
	public void close() {
		this.client.setScreen(this.parent);
	}

	@Override
	public void renderTooltip(final MatrixStack matrices, final ItemStack stack, final int x, final int y) {
		super.renderTooltip(matrices, stack, x, y);
	}

	public static void drawVerticalLine(final MatrixStack matrices, final int x, final int y1, final int y2, final int color) {
		DrawableHelper.drawVerticalLine(matrices, x, y1, y2, color);
	}

	public static void fill(final MatrixStack matrices, final int x1, final int y1, final int x2, final int y2, final int color) {
		WikiScreen.fill(matrices, x1, y1, x2, y2, 0, color);
	}

	public static void fill(final MatrixStack matrices, int x1, int y1, int x2, int y2, final int z, final int color) {
		int i;
		final Matrix4f matrix4f = matrices.peek().getPositionMatrix();
		if (x1 < x2) {
			i = x1;
			x1 = x2;
			x2 = i;
		}
		if (y1 < y2) {
			i = y1;
			y1 = y2;
			y2 = i;
		}
		final float f = ColorHelper.Argb.getAlpha(color) / 255.0f;
		final float g = ColorHelper.Argb.getRed(color) / 255.0f;
		final float h = ColorHelper.Argb.getGreen(color) / 255.0f;
		final float j = ColorHelper.Argb.getBlue(color) / 255.0f;
		final BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		RenderSystem.disableBlend();
		RenderSystem.setShader(GameRenderer::getPositionColorProgram);
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
		bufferBuilder.vertex(matrix4f, x1, y1, z).color(g, h, j, f).next();
		bufferBuilder.vertex(matrix4f, x1, y2, z).color(g, h, j, f).next();
		bufferBuilder.vertex(matrix4f, x2, y2, z).color(g, h, j, f).next();
		bufferBuilder.vertex(matrix4f, x2, y1, z).color(g, h, j, f).next();
		BufferRenderer.drawWithGlobalProgram(bufferBuilder.end());
		RenderSystem.disableBlend();
	}
}
