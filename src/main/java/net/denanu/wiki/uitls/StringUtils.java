package net.denanu.wiki.uitls;

public class StringUtils {
	public static String translate(final String translationKey, final Object... args)
	{
		return net.minecraft.client.resource.language.I18n.translate(translationKey, args);
	}
}
