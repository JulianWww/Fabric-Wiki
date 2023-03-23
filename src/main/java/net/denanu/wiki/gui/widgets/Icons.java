package net.denanu.wiki.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;

import net.denanu.wiki.Wiki;
import net.denanu.wiki.uitls.RenderUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public enum Icons {
	SUB_PAGES_CLOSED(Identifier.of(Wiki.MOD_ID, "textures/gui/pageselection.png"),  0, 0, 32, 32, 10, 5, 14, 22, 64, 64),
	SUB_PAGES_SHOWN( Identifier.of(Wiki.MOD_ID, "textures/gui/pageselection.png"), 32, 0, 32, 32, 5, 10, 22, 14, 64, 64);

	public static Identifier SMALL_BUTTONS = Identifier.of(Wiki.MOD_ID, "textures/gui/small_buttons.png");

	private Identifier id;
	private int x, y, w, h, boxX, boxY, boxW, boxH, texX, texY;

	Icons(final Identifier id, final int x, final int y, final int w, final int h, final int texX, final int texY) {
		this(id, x, y, w, h, 0, 0, w, h, texX, texY);
	}

	Icons(final Identifier id, final int x, final int y, final int w, final int h, final int boxX, final int boxY, final int boxW, final int boxH, final int texX, final int texY) {
		this.id = id;
		this.x = x;
		this.y = y;
		this.w = w;
		this.h = h;
		this.boxX = boxX;
		this.boxY = boxY;
		this.boxW = boxW;
		this.boxH = boxH;
		this.texX = texX;
		this.texY = texY;
	}

	public void render(final MatrixStack stack, final int xPos, final int yPos, final int mouseX, final int mouseY, final boolean hovered, final float scale) {
		RenderSystem.setShaderTexture(0,this.id);
		if (hovered && this.isOver(xPos, yPos, mouseX, mouseY, scale)) {
			DrawableHelper.drawTexture(stack, xPos, yPos, (int)(this.w * scale), (int)(this.h * scale), this.x, this.y + this.h, this.w, this.h, this.texX, this.texY);
		}
		else {
			DrawableHelper.drawTexture(stack, xPos, yPos, (int)(this.w * scale), (int)(this.h * scale), this.x, this.y, this.w, this.h, this.texX, this.texY);
		}
	}

	public boolean isOver(final int xPos, final int yPos, final int mouseX, final int mouseY, final float scale) {
		return RenderUtils.isHovered((int)(xPos + this.boxX * scale), (int)(yPos + this.boxY * scale), (int)(this.boxW * scale), (int)(this.boxH * scale), mouseX, mouseY);
	}
}
