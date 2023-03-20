package net.denanu.wiki.content;

import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class PageContents {
	private final List<PageElement> elements = new LinkedList<>();

	public PageContents (final JsonObject json) {
		for (final JsonElement element : json.getAsJsonArray("content")) {
			this.elements.add(new PageElement(element.getAsJsonObject()));
		}
	}

	public PageContents() {}

	public List<PageElement> getElements() {
		return this.elements;
	}
}
