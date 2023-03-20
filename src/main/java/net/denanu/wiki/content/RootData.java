package net.denanu.wiki.content;

import java.io.FileNotFoundException;

import com.google.gson.JsonObject;
import com.terraformersmc.modmenu.ModMenu;

import net.denanu.wiki.Wiki;
import net.denanu.wiki.uitls.JsonUtils;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class RootData extends PageContents {
	private Text title = Text.literal("tag missing");

	public RootData(final JsonObject json) {
		super(json);
		this.setTitle(json);
	}

	public RootData() {}

	public static RootData fromJson(final Identifier id) {
		try {
			final JsonObject json = JsonUtils.loadAssetJson(ModMenu.GSON, id);
			return new RootData(json);

		}
		catch (final FileNotFoundException e) {
			Wiki.LOGGER.error("missing wiki root file");
		}
		return new RootData();
	}

	private void setTitle(final JsonObject json) {
		if (JsonHelper.hasString(json, "title")) {
			this.title = Text.translatable(JsonHelper.getString(json, "title"));
		}
	}

	public Text getTitle() {
		return this.title;
	}
}
