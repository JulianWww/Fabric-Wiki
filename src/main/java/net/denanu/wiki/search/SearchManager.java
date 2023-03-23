package net.denanu.wiki.search;

import java.io.File;
import java.util.List;
import java.util.stream.Stream;

import org.apache.commons.text.similarity.LevenshteinDistance;

import net.denanu.wiki.gui.widgets.entries.WikiEntry;
import net.denanu.wiki.uitls.JsonUtils;
import net.denanu.wiki.uitls.ResourceUtils;
import net.minecraft.util.Identifier;

public class SearchManager {
	private final List<SearchEntry> entries;
	public static final LevenshteinDistance distanceFactory = new LevenshteinDistance();

	public SearchManager(final Identifier root) {
		final Stream<File> files = ResourceUtils.getResourceFolderFilesStream(JsonUtils.toWikiFiles(root));
		this.entries = files.map(file -> new SearchEntry(file, root.getNamespace())).toList();
	}

	public List<WikiEntry> filter(final String str) {
		return this.entries.stream().parallel().filter(entry -> {
			entry.calculateDistanceTo(str.toLowerCase());
			return entry.getDistance() <= (int)(str.length() * 0.5 + 1);
		}).sorted(new SearchEntryComparator()).map(SearchEntry::getId).map(id -> new WikiEntry(id, WikiEntry.Type.FILTERED)).toList();
	}
}
