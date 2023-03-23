package net.denanu.wiki.gui.widgets.data;

import net.denanu.wiki.mixin.LegacySmithingRecipeAccessor;
import net.denanu.wiki.mixin.SmithingTransformRecipeAccessor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.recipe.LegacySmithingRecipe;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.recipe.SmithingTransformRecipe;

// in preperation for 1.20
// TODO: 1.20 add the new recipes

@Environment(value=EnvType.CLIENT)
public class SmithingReipeProviderFactory {
	public static IngredientProvider build(final SmithingRecipe recipe) {
		if (recipe instanceof final SmithingTransformRecipe r) {
			return SmithingReipeProviderFactory.buildSmithingTransformRecipeProvider(r);
		}
		if (recipe instanceof final LegacySmithingRecipe r) {
			return SmithingReipeProviderFactory.buildLegacySmithingRecipeProvider(r);
		}

		return null;
	}

	@Deprecated(forRemoval=true)
	private static IngredientProvider buildLegacySmithingRecipeProvider(final LegacySmithingRecipe recipe) {
		final LegacySmithingRecipeAccessor r = (LegacySmithingRecipeAccessor)recipe;
		return idx -> switch (idx) {
		case 0 -> null;
		case 1 -> r.getBase();
		default -> r.getAddition();
		};
	}

	private static IngredientProvider buildSmithingTransformRecipeProvider(final SmithingTransformRecipe recipe) {
		final SmithingTransformRecipeAccessor r = (SmithingTransformRecipeAccessor)recipe;
		return idx -> switch (idx) {
		case 0 -> r.getAddition();
		case 1 -> r.getBase();
		default -> r.getTemplate();
		};
	}
}
