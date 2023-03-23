package net.denanu.wiki.uitls;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class StringUtils {
	public static String translate(final String translationKey, final Object... args)
	{
		return net.minecraft.client.resource.language.I18n.translate(translationKey, args);
	}

	public static String removeLast(final String str, final int n) {
		return str.substring(0, str.length() - n);
	}
}
