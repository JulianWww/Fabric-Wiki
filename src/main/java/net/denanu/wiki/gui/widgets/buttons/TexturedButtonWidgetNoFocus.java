package net.denanu.wiki.gui.widgets.buttons;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TexturedButtonWidgetNoFocus extends TexturedButtonWidget {
	public TexturedButtonWidgetNoFocus(final int x, final int y, final int width, final int height, final int u, final int v, final Identifier texture, final ButtonWidget.PressAction pressAction) {
		super(x, y, width, height, u, v, texture, pressAction);
	}

	public TexturedButtonWidgetNoFocus(final int x, final int y, final int width, final int height, final int u, final int v, final int hoveredVOffset, final Identifier texture, final ButtonWidget.PressAction pressAction) {
		super(x, y, width, height, u, v, hoveredVOffset, texture, pressAction);
	}

	public TexturedButtonWidgetNoFocus(final int x, final int y, final int width, final int height, final int u, final int v, final int hoveredVOffset, final Identifier texture, final int textureWidth, final int textureHeight, final ButtonWidget.PressAction pressAction) {
		super(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, pressAction);
	}

	public TexturedButtonWidgetNoFocus(final int x, final int y, final int width, final int height, final int u, final int v, final int hoveredVOffset, final Identifier texture, final int textureWidth, final int textureHeight, final ButtonWidget.PressAction pressAction, final Text message) {
		super(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, pressAction, message);
	}

	@Override
	public boolean isFocused() {
		return false;
	}
}
