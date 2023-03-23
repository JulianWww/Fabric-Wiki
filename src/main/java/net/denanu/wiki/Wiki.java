package net.denanu.wiki;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.denanu.wiki.api.WikiRootManager;
import net.denanu.wiki.config.WikiConfigManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class Wiki implements ClientModInitializer {
	public static String MOD_ID = "wiki";
	public static final Logger LOGGER = LoggerFactory.getLogger(Wiki.MOD_ID);

	@Override
	public void onInitializeClient() {
		WikiConfigManager.initializeConfig();
		WikiRootManager.setup();

	}
}
