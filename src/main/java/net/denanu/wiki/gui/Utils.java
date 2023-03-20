package net.denanu.wiki.gui;

import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gl.ShaderProgram;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;

public class Utils {
	public static void drawHorizontalLine(final MatrixStack matrices, final int x1, final int x2, final int y, final int color) {
		final ShaderProgram shader = RenderSystem.getShader();
		final Tessellator tessellator = Tessellator.getInstance();
		final BufferBuilder buffer = tessellator.getBuffer();
		RenderSystem.setShader(GameRenderer::getPositionProgram);
		final float r = (color >> 16 & 0xff) / 255.0f;
		final float g = (color >>  8 & 0xff) / 255.0f;
		final float b = (color & 0xff) / 255.0f;
		final float a = (color >> 24 & 0xff) / 255.0f;
		RenderSystem.setShaderColor(r, g, b, a);
		buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
		buffer.vertex(x1, y+1, 0.0D).color(color).next();
		buffer.vertex(x2, y+1, 0.0D).color(color).next();
		buffer.vertex(x2, y, 0.0D).color(color).next();
		buffer.vertex(x1, y, 0.0D).color(color).next();
		tessellator.draw();
		RenderSystem.setShader(() -> shader);
	}
}
