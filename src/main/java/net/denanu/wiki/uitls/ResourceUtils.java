package net.denanu.wiki.uitls;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.stream.Stream;

public class ResourceUtils{
	public static File[] getResourceFolderFiles (final String folder) {
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		final URL url = loader.getResource(folder);
		final String path = url.getPath();
		return new File(path).listFiles();
	}

	public static Stream<File> getResourceFolderFilesStream (final String folder) {
		return Arrays.stream(ResourceUtils.getResourceFolderFiles(folder));
	}
}