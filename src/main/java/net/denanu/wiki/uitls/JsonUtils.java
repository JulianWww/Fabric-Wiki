package net.denanu.wiki.uitls;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.util.Identifier;

public class JsonUtils {
	public static String toWikiFile(final Identifier id) {
		return new StringBuilder().append("assets/").append(id.getNamespace()).append("/wiki/").append(id.getPath()).append(".json").toString();
	}

	public static JsonObject loadAssetJson(final Gson gson, final Identifier id) throws FileNotFoundException {
		final BufferedReader br = new BufferedReader(new FileReader(new File(JsonUtils.class.getClassLoader().getResource(JsonUtils.toWikiFile(id)).getFile())));
		return new JsonParser().parse(br).getAsJsonObject();
	}
}
