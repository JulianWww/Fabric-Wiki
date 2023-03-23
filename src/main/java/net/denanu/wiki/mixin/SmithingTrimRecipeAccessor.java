package net.denanu.wiki.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.SmithingTrimRecipe;

@Mixin(SmithingTrimRecipe.class)
public interface SmithingTrimRecipeAccessor {
	@Accessor Ingredient getTemplate();
	@Accessor Ingredient getBase();
	@Accessor Ingredient getAddition();
}
