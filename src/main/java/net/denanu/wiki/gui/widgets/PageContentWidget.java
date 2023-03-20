package net.denanu.wiki.gui.widgets;

import java.time.Instant;

import org.apache.commons.lang3.mutable.MutableInt;

import net.denanu.wiki.Wiki;
import net.denanu.wiki.content.PageContents;
import net.denanu.wiki.content.PageElement;
import net.denanu.wiki.gui.widgets.data.CraftingBackgrounds;
import net.denanu.wiki.gui.widgets.data.IngredientProvider;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

public class PageContentWidget extends ScrollableWidget {
	private final PageContents contents;
	private int contentHeight;
	private final ItemRenderer itemRenderer;
	private int stackIdx;

	private static Identifier CRAFTING = Identifier.of(Wiki.MOD_ID, "textures/gui/crafting.png");
	private static Identifier FURNACE = Identifier.of(Wiki.MOD_ID, "textures/gui/furnace.png");

	public PageContentWidget(final int x, final int y, final int w, final int h, final PageContents content, final ItemRenderer itemRenderer) {
		super(x, y, w, h, null);
		this.contents = content;
		this.contentHeight = 1;
		this.itemRenderer = itemRenderer;
		this.stackIdx = 0;
	}

	@Override
	protected int getContentsHeight() {
		return Math.max(1, this.contentHeight);
	}

	@Override
	protected int getMaxScrollY() {
		return Math.max(1, super.getMaxScrollY());
	}

	@Override
	protected boolean overflows() {
		return true;
	}

	@Override
	protected double getDeltaYPerScroll() {
		return 4;
	}

	@Override
	protected void renderContents(final MatrixStack stack, final int mousex, final int mousey, final float delta) {
		final MutableInt x = new MutableInt(this.getX() + 4);
		final MutableInt y = new MutableInt(this.getY() + 4);

		for (final PageElement element : this.contents.getElements()) {
			element.getStyle().render(this, stack, x, y, this.width, element);
		}
		this.contentHeight = y.intValue();

		this.stackIdx = (int)Instant.now().getEpochSecond();
	}

	@Override
	protected void appendClickableNarrations(final NarrationMessageBuilder var1) {
	}

	public enum Style {
		normal(PageContentWidget::renderNormal,			net.minecraft.text.Style.EMPTY),
		h1(PageContentWidget.renderSection(2, 18), 		net.minecraft.text.Style.EMPTY.withUnderline(true)),
		h2(PageContentWidget.renderSection(1.7f, 15), 	net.minecraft.text.Style.EMPTY.withUnderline(true)),
		h3(PageContentWidget.renderSection(1.5f, 15), 	net.minecraft.text.Style.EMPTY.withUnderline(true)),
		h4(PageContentWidget.renderSection(1.3f, 15), 	net.minecraft.text.Style.EMPTY.withUnderline(true)),
		h5(PageContentWidget.renderSection(1.15f, 15), 	net.minecraft.text.Style.EMPTY.withUnderline(true)),
		h6(PageContentWidget.renderSection(1, 11), 		net.minecraft.text.Style.EMPTY.withUnderline(true)),
		recipe(PageContentWidget::renderCrafting,		net.minecraft.text.Style.EMPTY);

		StyleRenderer renderer;
		net.minecraft.text.Style style;

		Style(final StyleRenderer renderer, final net.minecraft.text.Style style) {
			this.renderer = renderer;
			this.style = style;
		}

		public net.minecraft.text.Style getStyle() {
			return this.style;
		}

		void render(final PageContentWidget page, final MatrixStack stack, final MutableInt x, final MutableInt y, final int width, final PageElement element) {
			this.renderer.render(page, stack, x, y, width, element);
		}

		private interface StyleRenderer{
			void render(PageContentWidget page, MatrixStack stack, MutableInt x, MutableInt y, int width, PageElement element);
		}
	}

	public static TextRenderer getTextRenderer() {
		final MinecraftClient instance = MinecraftClient.getInstance();
		return instance.textRenderer;
	}

	private int getLeft() {
		return this.getX() + 4;
	}

	public void setLeft(final MutableInt x) {
		x.setValue(this.getLeft());
	}

	public void newLine(final MutableInt y, final int delta) {
		if (y.intValue() > this.getY() + 4) {
			y.add(delta);
		}
	}

	public void newLine(final MutableInt y) {
		this.newLine(y, 10);
	}

	private static void renderTextBlock(final TextRenderer renderer, final MatrixStack stack, final MutableInt x, final MutableInt y, final int leftx, final int width, final PageElement element) {
		PageContentWidget.renderTextBlock(renderer, stack, x, y, leftx, width, element, 1f);
	}

	private static void renderTextBlock(final TextRenderer renderer, final MatrixStack stack, final MutableInt x, final MutableInt y, final int leftx, final int width, final PageElement element, final float scale) {
		PageContentWidget.renderTextBlock(renderer, stack, x, y, (int)(leftx / scale), width, element, 10, scale);
	}

	private static void renderTextBlock(final TextRenderer renderer, final MatrixStack stack, final MutableInt x, final MutableInt y, final int leftx, final int width, final PageElement element, final int lineSpaceing, final float scale) {
		final Text space = Text.literal(" ").setStyle(element.getStyle().getStyle());
		final int spaceSize = renderer.getWidth(space);
		boolean first = true;

		for (final String txt: element.getTxt()) {
			final Text text = Text.literal(txt).setStyle(element.getStyle().getStyle());
			final int txtWidth = renderer.getWidth(text);
			if (x.intValue() + txtWidth > (width - 14 + leftx) / scale) {
				x.setValue(leftx);
				y.add(lineSpaceing);
			}
			else if (!first) {
				renderer.draw(stack, space, x.intValue(), y.intValue(), 16777215);
				x.add(spaceSize);
			}
			renderer.draw(stack, text, x.intValue(), y.intValue(), 16777215);
			x.add(txtWidth);
			first = false;
		}
	}

	private static void renderNormal(final PageContentWidget page, final MatrixStack stack, final MutableInt x, final MutableInt y, final int width, final PageElement element) {
		final TextRenderer renderer = PageContentWidget.getTextRenderer();

		PageContentWidget.renderTextBlock(renderer, stack, x, y, page.getLeft(), width, element);
	}

	private static Style.StyleRenderer renderSection(final float scale, final int topPadding) {
		return (final PageContentWidget page, final MatrixStack stack, final MutableInt x, final MutableInt y, final int width, final PageElement element) -> {
			page.newLine(y, topPadding);
			page.setLeft(x);

			final TextRenderer renderer = PageContentWidget.getTextRenderer();
			stack.push();
			stack.scale(scale, scale, scale);

			final MutableInt xpos = new MutableInt(x.intValue() / scale);
			final int lastY = (int) (y.intValue() / scale);
			final MutableInt ypos = new MutableInt(lastY);

			PageContentWidget.renderTextBlock(renderer, stack, xpos, ypos, page.getLeft(), width, element, scale);
			stack.pop();

			page.setLeft(x);
			y.add((ypos.intValue() - lastY + 12) * scale);
		};
	}

	private void renderItem(final MatrixStack stack, final ItemStack itm, final int x, final int y, final int xpos, final int ypos) {
		this.itemRenderer.renderGuiItemIcon(stack, itm, xpos + 18*x, ypos + 18*y);
	}

	public static void renderCrafting(final PageContentWidget page, final CraftingBackgrounds bg, final MatrixStack stack, final int posx, final int posy, final IngredientProvider ingredientProvider, final ItemStack output) {
		for (int idxX = 0; idxX < 3; idxX++) {
			for (int idxY = 0; idxY < 3; idxY++) {
				final Ingredient ingredient = ingredientProvider.get(idxX + idxY * 3);
				if (ingredient!= null && ingredient.getMatchingStacks().length > 0) {
					page.renderItem(stack, ingredient.getMatchingStacks()[page.stackIdx % ingredient.getMatchingStacks().length], idxX, idxY, posx + 10, posy + 10);
				}
			}
		}
		page.renderItem(stack, output, 0, 0, posx + 104, posy + 28);
	}

	void renderBackdrop(final CraftingBackgrounds bg, final MatrixStack stack, final MutableInt x, final MutableInt y, final IngredientProvider ingredientProvider, final ItemStack output) {
		this.newLine(y);
		this.setLeft(x);

		final int h = bg.h;
		final int w = bg.w;
		final int posx = (this.width - w) / 2 + this.getLeft();
		final int posy = y.intValue();

		bg.crafter.renderCrafting(this, bg, stack, posx, posy, ingredientProvider, output);

		this.drawTexture(stack, bg.texture, posx, y.intValue(), 0, 0, 0, w, h, w, h);

		y.add(h + 5);
	}

	private static void renderCrafting(final PageContentWidget page, final MatrixStack stack, final MutableInt x, final MutableInt y, final int width, final PageElement element) {
		final Recipe<?> recipe = element.getRecipe();
		if (recipe instanceof final CraftingRecipe crafting) {

			if (crafting instanceof final ShapedRecipe shape) {
				page.renderBackdrop(CraftingBackgrounds.CRAFTING, stack, x, y, pos -> shape.getIngredients().get(pos), shape.getOutput(null));
			}

			else if (crafting instanceof final ShapelessRecipe unshaped) {
				page.renderBackdrop(CraftingBackgrounds.CRAFTING, stack, x, y, pos -> {
					final DefaultedList<Ingredient> ingreds = unshaped.getIngredients();
					if (ingreds.size() > pos) {
						return ingreds.get(pos);
					}
					return null;
				}, unshaped.getOutput(null));
			}
		}

		else if (recipe instanceof final AbstractCookingRecipe cooking) {

		}
	}
}
