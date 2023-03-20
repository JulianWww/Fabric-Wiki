package net.denanu.wiki.api;

import java.util.HashMap;

import net.denanu.wiki.Wiki;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.minecraft.util.Identifier;

public class WikiRootManager {
	public static HashMap<String, Identifier> WIKI_ROOTS = new HashMap<>();

	public static void setup() {
		Wiki.LOGGER.info("Building Wiki factories");

		FabricLoader.getInstance().getAllMods().forEach(mod -> {
			if (mod.getMetadata().containsCustomValue("wiki")) {
				final CustomValue.CvObject config = mod.getMetadata().getCustomValue("wiki").getAsObject();

				try {
					WikiRootManager.WIKI_ROOTS.put(mod.getMetadata().getId(), Identifier.of(mod.getMetadata().getId(), config.get("root").getAsString()));
					Wiki.LOGGER.info("Build Root for " + mod.getMetadata().getId());
				}
				catch (final Exception e){
					Wiki.LOGGER.error(
							config.get("root").getAsString() +
							" is not a valid Identifier"
							);
					throw e;
				}
			}
		});
	}

	public static Identifier getRoot(final String key) {
		return WikiRootManager.WIKI_ROOTS.get(key);
	}
}
