package net.denanu.wiki.content;

import java.util.Optional;

import com.google.gson.JsonObject;

import net.denanu.wiki.gui.widgets.PageContentWidget.Style;
import net.denanu.wiki.uitls.StringUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.recipe.Recipe;
import net.minecraft.util.Identifier;

public class PageElement {
	private Style style;
	private String translationKey;
	private String[] txt;
	private Recipe<?> recipe;
	public ClickableWidget widget;

	public PageElement(final JsonObject json) {
		this.setStyle(json);
		this.setText(json);
		this.setRecipe(json);
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
}
