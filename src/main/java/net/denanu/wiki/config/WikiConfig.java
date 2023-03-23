package net.denanu.wiki.config;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;

import com.terraformersmc.modmenu.config.option.OptionConvertable;

import net.minecraft.client.option.SimpleOption;

public class WikiConfig {
	public static SimpleOption<?>[] asOptions() {
		final ArrayList<SimpleOption<?>> options = new ArrayList<>();
		for (final Field field : WikiConfig.class.getDeclaredFields()) {
			if (Modifier.isStatic(field.getModifiers())
					&& Modifier.isFinal(field.getModifiers())
					&& OptionConvertable.class.isAssignableFrom(field.getType())
					&& !"HIDE_CONFIG_BUTTONS".equals(field.getName())
					&& !"MODIFY_TITLE_SCREEN".equals(field.getName())
					&& !"MODIFY_GAME_MENU".equals(field.getName())
					&& !"CONFIG_MODE".equals(field.getName())
					&& !"DISABLE_DRAG_AND_DROP".equals(field.getName())
					) {
				try {
					options.add(((OptionConvertable) field.get(null)).asOption());
				} catch (final IllegalAccessException e) {
					e.printStackTrace();
				}
			}
		}
		return options.stream().toArray(SimpleOption[]::new);
	}

	public static int getColor() {
		return 0xFFFFFFFF;
	}
}
