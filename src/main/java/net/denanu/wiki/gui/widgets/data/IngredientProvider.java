package net.denanu.wiki.gui.widgets.data;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.recipe.Ingredient;

@Environment(value=EnvType.CLIENT)
public interface IngredientProvider {
	Ingredient get(int key);
}
