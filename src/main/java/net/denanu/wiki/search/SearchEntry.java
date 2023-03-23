package net.denanu.wiki.search;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonObject;

import net.denanu.wiki.Wiki;
import net.denanu.wiki.uitls.IntComparator;
import net.denanu.wiki.uitls.JsonUtils;
import net.denanu.wiki.uitls.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class SearchEntry {
	private List<String> keywords;
	private int distance = 10;
	private final Identifier id;

	public SearchEntry(final File file, final String namespace) {
		final JsonObject json = JsonUtils.load(file);
		if (json.has("keywords")) {
			this.keywords = json.getAsJsonArray("keywords").asList().stream().parallel().map(element -> StringUtils.translate(new Identifier(element.getAsString()).toTranslationKey("keyword")).toLowerCase()).toList();
		}
		else {
			this.keywords = new ArrayList<>(1);
		}
		this.id = SearchEntry.getId(file, namespace);
		this.keywords.add(StringUtils.translate(this.id.toTranslationKey("wiki")));
	}

	private static Identifier getId(final File file, final String namespace) {
		return Identifier.of(namespace, StringUtils.removeLast(file.getName(), 5));
	}

	public void calculateDistanceTo(final String str) {
		this.distance = this.keywords.stream()
				.parallel()
				.map(word -> {
					if (str.length() < word.length()) {
						word = word.substring(0, str.length());
					}

					return SearchManager.distanceFactory.apply(word, str);
				})
				.min(new IntComparator())
				.orElseGet(() -> 10);
		Wiki.LOGGER.info(Integer.toString(this.distance));
	}

	public int getDistance() {
		return this.distance;
	}

	public Identifier getId() {
		return this.id;
	}
}
