package net.denanu.wiki.uitls;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.denanu.wiki.Wiki;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public class JsonUtils {
	public static String toWikiFile(final Identifier id) {
		return JsonUtils.toWikiFilesBuilder(id).append(id.getPath()).append(".json").toString();
	}

	public static String toWikiFiles(final Identifier id) {
		return JsonUtils.toWikiFilesBuilder(id).toString();
	}
	public static StringBuilder toWikiFilesBuilder(final Identifier id) {
		return new StringBuilder().append("assets/").append(id.getNamespace()).append("/wiki/");
	}

	public static JsonObject loadAssetJson(final Gson gson, final Identifier id) throws FileNotFoundException {
		final BufferedReader br = new BufferedReader(new FileReader(new File(JsonUtils.class.getClassLoader().getResource(JsonUtils.toWikiFile(id)).getFile())));
		return new JsonParser().parse(br).getAsJsonObject();
	}

	public static JsonObject load(final File file) {
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(file));
			return new JsonParser().parse(br).getAsJsonObject();
		} catch (final FileNotFoundException e) {
			Wiki.LOGGER.error("Please open an Issue for this error as a previously checked file has suddenly dissapeared", e);
		}
		return null;
	}
}
