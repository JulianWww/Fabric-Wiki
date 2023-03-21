package net.denanu.wiki.content;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

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

	public List<PageElement> getElements() {
		return this.elements;
	}

	public List<String> getSubPages() {
		return this.subPages;
	}
}
