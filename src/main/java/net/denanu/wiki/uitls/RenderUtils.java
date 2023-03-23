package net.denanu.wiki.uitls;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class RenderUtils {
	public static boolean isHovered(final int x, final int y, final int w, final int h, final int mx, final int my) {
		return x < mx && mx < x + w && y < my && my < y + h;
	}
}
