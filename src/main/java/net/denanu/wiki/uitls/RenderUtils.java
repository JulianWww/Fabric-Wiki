package net.denanu.wiki.uitls;

public class RenderUtils {
	public static boolean isHovered(final int x, final int y, final int w, final int h, final int mx, final int my) {
		boolean a = x < mx;
				boolean b = mx < x + w;
				boolean c = y < my;
				boolean d = my < y + h;
		return x < mx && mx < x + w && y < my && my < y + h;
	}
}
