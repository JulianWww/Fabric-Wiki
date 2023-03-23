package net.denanu.wiki.mixin;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import com.terraformersmc.modmenu.gui.ModsScreen;
import com.terraformersmc.modmenu.gui.widget.entries.ModListEntry;

@Mixin(ModsScreen.class)
public interface ModsScreenAccessor {
	@Accessor ModListEntry getSelected();
	@Accessor Map<String, Boolean> getModHasConfigScreen();
	@Accessor Map<String, Throwable> getModScreenErrors();
}
