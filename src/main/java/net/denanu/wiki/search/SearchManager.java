package net.denanu.wiki.search;

import java.io.File;
import java.util.stream.Stream;

import net.denanu.wiki.uitls.JsonUtils;
import net.denanu.wiki.uitls.ResourceUtils;
import net.minecraft.util.Identifier;

public class SearchManager {
	public SearchManager(final Identifier root) {
		final Stream<File> files = ResourceUtils.getResourceFolderFilesStream(JsonUtils.toWikiFiles(root));
		files.map(file -> new SearchEntry(file, root.getNamespace())).toList();
	}
}
