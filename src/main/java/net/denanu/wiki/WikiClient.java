package net.denanu.wiki;

import net.denanu.wiki.api.WikiRootManager;
import net.denanu.wiki.config.WikiConfigManager;
import net.fabricmc.api.ClientModInitializer;

public class WikiClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		WikiConfigManager.initializeConfig();
		WikiRootManager.setup();

	}
}
