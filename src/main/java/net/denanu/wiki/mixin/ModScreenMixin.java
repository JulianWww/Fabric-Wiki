package net.denanu.wiki.mixin;

import java.util.Objects;

import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.systems.RenderSystem;
import com.terraformersmc.modmenu.gui.ModsScreen;

import net.denanu.wiki.Wiki;
import net.denanu.wiki.api.WikiRootManager;
import net.denanu.wiki.gui.WikiScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

@Mixin(ModsScreen.class)
public abstract class ModScreenMixin extends Screen {
	protected ModScreenMixin(final Text title) {
		super(title);
	}

	private static final Identifier WIKI_BUTTON_LOCATION = new Identifier(Wiki.MOD_ID, "textures/gui/wiki_button.png");
	private static final Text WIKI = Text.translatable("wiki.wiki.open");

	@Nullable
	private static TexturedButtonWidget WIKI_BUTTON;

	@Inject(method="init", at=@At("TAIL"))
	public void init(final CallbackInfo ci) {
		final ModsScreen screen = (ModsScreen)(Object)this;

		final TexturedButtonWidget wiki_button = new TexturedButtonWidget(screen.width - 46, 48, 20, 20, 0, 0, 20, ModScreenMixin.WIKI_BUTTON_LOCATION, 32, 64, button -> {
			final String id = Objects.requireNonNull(((ModsScreenAccessor)ModScreenMixin.this).getSelected()).getMod().getId();
			final Identifier root = WikiRootManager.getRoot(id);
			if (root != null) {
				final WikiScreen wikiScreen = WikiScreen.of(root, screen);
				this.client.setScreen(wikiScreen);
			} else {
				button.active = false;
			}
		}) {
			@Override
			public void render(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
				final String modId = ((ModsScreenAccessor)ModScreenMixin.this).getSelected().getMod().getId();
				if (((ModsScreenAccessor)ModScreenMixin.this).getSelected() != null) {
					this.active = WikiRootManager.getRoot(modId) != null;

					this.visible = ((ModsScreenAccessor)ModScreenMixin.this).getSelected() != null && WikiRootManager.getRoot(modId) != null;

					if (this.visible) {
						if (((ModsScreenAccessor)ModScreenMixin.this).getModHasConfigScreen().get(modId) || ((ModsScreenAccessor)ModScreenMixin.this).getModScreenErrors().containsKey(modId)) {
							this.setX(screen.width - 46);
						}
						else {
							this.setX(screen.width - 24);
						}
					}
				} else {
					this.active = false;
					this.visible = false;
				}

				super.render(matrices, mouseX, mouseY, delta);
			}

			@Override
			public void renderButton(final MatrixStack matrices, final int mouseX, final int mouseY, final float delta) {
				RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
				RenderSystem.setShaderColor(1, 1, 1, 1f);
				super.renderButton(matrices, mouseX, mouseY, delta);
			}
		};

		wiki_button.setTooltip(Tooltip.of(ModScreenMixin.WIKI));
		this.addDrawableChild(wiki_button);
	}
}
