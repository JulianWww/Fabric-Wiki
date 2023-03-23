package net.denanu.wiki.content;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.terraformersmc.modmenu.ModMenu;

import net.denanu.wiki.Wiki;
import net.denanu.wiki.uitls.JsonUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class PageContents {
	private final List<PageElement> elements = new LinkedList<>();
	private final List<String> subPages = new LinkedList<>();

	public PageContents (final JsonObject json) {
		for (final JsonElement element : json.getAsJsonArray("content")) {
			this.elements.add(new PageElement(element.getAsJsonObject()));
		}
		if (json.has("pages")) {
			for (final JsonElement element : json.getAsJsonArray("pages")) {
				this.subPages.add(element.getAsString());
			}
		}
	}

	public PageContents() {}

	public static PageContents fromJson(final Identifier id) {
		try {
			final JsonObject json = JsonUtils.loadAssetJson(ModMenu.GSON, id);
			return new PageContents(json);

		}
		catch (final Exception e) {
			Wiki.LOGGER.error("wiki is missing file " + JsonUtils.toWikiFile(id) + " because", e);
		}
		return new PageContents();
	}

	public List<PageElement> getElements() {
		return this.elements;
	}

	public List<String> getSubPages() {
		return this.subPages;
	}
}
