package net.denanu.wiki.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;

@Environment(value=EnvType.CLIENT)
public interface WikiFactory {
	Identifier getRoot();
}
