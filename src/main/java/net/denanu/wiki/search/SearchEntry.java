package net.denanu.wiki.search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import net.denanu.wiki.uitls.JsonUtils;
import net.denanu.wiki.uitls.StringUtils;
import net.minecraft.util.Identifier;

public class SearchEntry {
	List<String> keywords;


	public SearchEntry(final File file, final String namespace) {
		final JsonObject json = JsonUtils.load(file);
		if (json.has("keywords")) {
			this.keywords = json.getAsJsonArray("keywords").asList().stream().parallel().map(element -> StringUtils.translate(new Identifier(element.getAsString()).toTranslationKey("keyword"))).toList();
		}
		else {
			this.keywords = new ArrayList<>(1);
		}
		this.keywords.add(StringUtils.translate(SearchEntry.getId(file, namespace).toTranslationKey("keyword")));
	}

	private static Identifier getId(final File file, final String namespace) {
		return Identifier.of(namespace, StringUtils.removeLast(file.getName(), 5));
	}

	public int calculateDistanceTo(final String str) {
		return 0;
	}
}
