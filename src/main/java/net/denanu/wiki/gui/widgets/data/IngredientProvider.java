package net.denanu.wiki.gui.widgets.data;

import net.minecraft.recipe.Ingredient;

public interface IngredientProvider {
	Ingredient get(int key);
}
