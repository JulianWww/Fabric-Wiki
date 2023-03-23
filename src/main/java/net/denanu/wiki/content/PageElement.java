package net.denanu.wiki.content;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.denanu.wiki.gui.widgets.PageContentWidget.Style;
import net.denanu.wiki.uitls.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class PageElement {
	private Style style;
	private String translationKey;
	private String[] txt;
	private Recipe<?> recipe;
	private List<Item> blocks;

	public PageElement(final JsonObject json) {
		this.setStyle(json);
		this.setText(json);
		this.setBlocks(json);
		this.setRecipe(json);
	}

	private void setBlocks(final JsonObject json) {
		this.blocks = new LinkedList<>();
		if (json.has("blocks")) {
			final JsonElement jsonBlocks = json.get("blocks");
			final Consumer<JsonElement> add = block -> this.blocks.add(Registries.ITEM.get(new Identifier(block.getAsString())));

			if (jsonBlocks.isJsonArray()) {
				for (final JsonElement block : jsonBlocks.getAsJsonArray()) {
					add.accept(block);
				}
			}
			else {
				add.accept(jsonBlocks);
			}
		}
	}

	private void setText(final JsonObject json) {
		if (json.has("text")) {
			this.translationKey = json.get("text").getAsString();
			this.txt = StringUtils.translate(this.translationKey).split(" ");
		}
		else {
			//Wiki.LOGGER.error("Page contents must have 'content' tag");
			this.translationKey = "";
		}
	}

	private void setStyle(final JsonObject json) {
		if (json.has("style")) {
			this.style = Style.valueOf(json.get("style").getAsString());
		}
		if (this.style == null) {
			this.style = Style.normal;
		}
	}

	private void setRecipe(final JsonObject json) {
		if (json.has("recipe")) {
			final MinecraftClient client = MinecraftClient.getInstance();
			if (client.player != null) {
				final Optional<? extends Recipe<?>> recipeOption = client.player.getWorld().getRecipeManager().get(new Identifier(json.get("recipe").getAsString()));
				this.recipe = recipeOption.get();
				if (this.recipe != null && this.blocks.size() == 0) {
					if (this.recipe.getType() == RecipeType.CRAFTING) {
						this.blocks.add(Items.CRAFTING_TABLE);
					}
					else if (this.recipe.getType() == RecipeType.CAMPFIRE_COOKING) {
						this.blocks.add(Items.CAMPFIRE);
					}
					else if (this.recipe.getType() == RecipeType.STONECUTTING) {
						this.blocks.add(Items.STONECUTTER);
					}
					else if (this.recipe.getType() == RecipeType.SMITHING) {
						this.blocks.add(Items.SMITHING_TABLE);
					}
				}
			}
			else {
				//final ResourcePackManager resourcePackManager = VanillaDataPackProvider.createManager(session.getDirectory(WorldSavePath.DATAPACKS));
			}
		}
	}


	public Style getStyle() {
		return this.style;
	}

	public String[] getTxt() {
		return this.txt;
	}

	public Recipe<?> getRecipe() {
		return this.recipe;
	}

	public String getTranslationKey() {
		return this.translationKey;
	}

	public List<Item> getCrafters() {
		return this.blocks;
	}
}
