package net.denanu.wiki.gui.widgets.data;

import net.denanu.wiki.gui.widgets.PageContentWidget;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

@Environment(value=EnvType.CLIENT)
public interface CraftingPlacesRenderer {
	void renderCrafting(PageContentWidget page, CraftingBackgrounds bg, final MatrixStack stack, final int posx, final int posy, final IngredientProvider ingredientProvider, final ItemStack output, int mousex, int mousey);
}
