package net.denanu.wiki.config;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Locale;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.terraformersmc.modmenu.ModMenu;
import com.terraformersmc.modmenu.config.option.BooleanConfigOption;
import com.terraformersmc.modmenu.config.option.ConfigOptionStorage;
import com.terraformersmc.modmenu.config.option.EnumConfigOption;
import com.terraformersmc.modmenu.config.option.StringSetConfigOption;

import net.denanu.wiki.Wiki;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;

@Environment(value=EnvType.CLIENT)
public class WikiConfigManager {
	private static File file;

	private static void prepareConfigFile() {
		if (WikiConfigManager.file != null) {
			return;
		}
		WikiConfigManager.file = new File(FabricLoader.getInstance().getConfigDir().toFile(), Wiki.MOD_ID + ".json");
	}

	public static void initializeConfig() {
		WikiConfigManager.load();
	}

	@SuppressWarnings("unchecked")
	private static void load() {
		WikiConfigManager.prepareConfigFile();

		try {
			if (!WikiConfigManager.file.exists()) {
				WikiConfigManager.save();
			}
			if (WikiConfigManager.file.exists()) {
				final BufferedReader br = new BufferedReader(new FileReader(WikiConfigManager.file));
				final JsonObject json = new JsonParser().parse(br).getAsJsonObject();

				for (final Field field : WikiConfig.class.getDeclaredFields()) {
					if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
						if (StringSetConfigOption.class.isAssignableFrom(field.getType())) {
							final JsonArray jsonArray = json.getAsJsonArray(field.getName().toLowerCase(Locale.ROOT));
							if (jsonArray != null) {
								final StringSetConfigOption option = (StringSetConfigOption) field.get(null);
								ConfigOptionStorage.setStringSet(option.getKey(), Sets.newHashSet(jsonArray).stream().map(JsonElement::getAsString).collect(Collectors.toSet()));
							}
						} else if (BooleanConfigOption.class.isAssignableFrom(field.getType())) {
							final JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive(field.getName().toLowerCase(Locale.ROOT));
							if (jsonPrimitive != null && jsonPrimitive.isBoolean()) {
								final BooleanConfigOption option = (BooleanConfigOption) field.get(null);
								ConfigOptionStorage.setBoolean(option.getKey(), jsonPrimitive.getAsBoolean());
							}
						} else if (EnumConfigOption.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType) {
							final JsonPrimitive jsonPrimitive = json.getAsJsonPrimitive(field.getName().toLowerCase(Locale.ROOT));
							if (jsonPrimitive != null && jsonPrimitive.isString()) {
								final Type generic = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
								if (generic instanceof Class<?>) {
									final EnumConfigOption<?> option = (EnumConfigOption<?>) field.get(null);
									Enum<?> found = null;
									for (final Enum<?> value : ((Class<Enum<?>>) generic).getEnumConstants()) {
										if (value.name().toLowerCase(Locale.ROOT).equals(jsonPrimitive.getAsString())) {
											found = value;
											break;
										}
									}
									if (found != null) {
										ConfigOptionStorage.setEnumTypeless(option.getKey(), found);
									}
								}
							}
						}
					}
				}
			}
		} catch (FileNotFoundException | IllegalAccessException e) {
			System.err.println("Couldn't load Mod Menu configuration file; reverting to defaults");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static void save() {
		WikiConfigManager.prepareConfigFile();

		final JsonObject config = new JsonObject();

		try {
			for (final Field field : WikiConfig.class.getDeclaredFields()) {
				if (Modifier.isStatic(field.getModifiers()) && Modifier.isFinal(field.getModifiers())) {
					if (BooleanConfigOption.class.isAssignableFrom(field.getType())) {
						final BooleanConfigOption option = (BooleanConfigOption) field.get(null);
						config.addProperty(field.getName().toLowerCase(Locale.ROOT), ConfigOptionStorage.getBoolean(option.getKey()));
					} else if (StringSetConfigOption.class.isAssignableFrom(field.getType())) {
						final StringSetConfigOption option = (StringSetConfigOption) field.get(null);
						final JsonArray array = new JsonArray();
						ConfigOptionStorage.getStringSet(option.getKey()).forEach(array::add);
						config.add(field.getName().toLowerCase(Locale.ROOT), array);
					} else if (EnumConfigOption.class.isAssignableFrom(field.getType()) && field.getGenericType() instanceof ParameterizedType) {
						final Type generic = ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0];
						if (generic instanceof Class<?>) {
							final EnumConfigOption<?> option = (EnumConfigOption<?>) field.get(null);
							config.addProperty(field.getName().toLowerCase(Locale.ROOT), ConfigOptionStorage.getEnumTypeless(option.getKey(), (Class<Enum<?>>) generic).name().toLowerCase(Locale.ROOT));
						}
					}
				}
			}
		} catch (final IllegalAccessException e) {
			e.printStackTrace();
		}

		final String jsonString = ModMenu.GSON.toJson(config);

		try (FileWriter fileWriter = new FileWriter(WikiConfigManager.file)) {
			fileWriter.write(jsonString);
		} catch (final IOException e) {
			System.err.println("Couldn't save Mod Menu configuration file");
			e.printStackTrace();
		}
	}
}
