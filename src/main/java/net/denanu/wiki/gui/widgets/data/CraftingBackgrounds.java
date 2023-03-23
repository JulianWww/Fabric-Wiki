package net.denanu.wiki.gui.widgets.data;

import net.denanu.wiki.Wiki;
import net.denanu.wiki.gui.widgets.PageContentWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public enum CraftingBackgrounds {
	CRAFTING("textures/gui/crafting.png", PageContentWidget::renderCrafting, 134, 71),
	FURNACE("textures/gui/furnace.png", PageContentWidget::renderSmelting, 101, 73),
	CAMPFIRE("textures/gui/campfire.png", PageContentWidget::renderCampfire, 101, 43),
	SMITHING("textures/gui/smithing.png", PageContentWidget::renderSmithing, 161, 75);

	public final Identifier texture;
	public final int w, h;
	public final CraftingPlacesRenderer crafter;

	CraftingBackgrounds(final String texture, final CraftingPlacesRenderer crafter, final int w, final int h) {
		this.texture = Identifier.of(Wiki.MOD_ID, texture);
		this.w = w;
		this.h = h;
		this.crafter = crafter;
	}
}
