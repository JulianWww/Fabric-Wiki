package net.denanu.wiki.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.terraformersmc.modmenu.gui.ModsScreen;

import net.denanu.wiki.content.RootData;
import net.denanu.wiki.gui.widgets.PageContentWidget;
import net.denanu.wiki.gui.widgets.PageListWidget;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class WikiScreen extends Screen {
	public final RootData root;
	private PageContentWidget contents;
	private PageListWidget pageList;
	protected final Screen parent;

	protected WikiScreen(final Screen parent, final RootData root) {
		super(root.getTitle());
		this.root = root;
		this.parent = parent;

	}

	public static WikiScreen of(final Identifier root, final ModsScreen screen) {
		final RootData rootData = RootData.fromJson(root);
		return new WikiScreen(screen, rootData);
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
		this.pageList = new PageListWidget(this.client, 10, 10, 0, 10, 10);
		this.pageList.setLeftPos(4);

		this.addDrawableChild(this.contents);
		this.addDrawableChild(this.pageList);
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

		matrices.push();
		matrices.scale(2f, 2f, 0);
		DrawableHelper.drawCenteredTextWithShadow(matrices, this.textRenderer, this.title, this.width / 4, 2, 16777215);
		matrices.pop();
	}

	@Override
	public void renderBackground(final MatrixStack matrices) {
		WikiScreen.overlayBackground(0, 0, this.width, this.height, 32, 32, 32, 255, DrawableHelper.OPTIONS_BACKGROUND_TEXTURE);
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
}
