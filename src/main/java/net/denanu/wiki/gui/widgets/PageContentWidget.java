package net.denanu.wiki.gui.widgets;

import java.time.Instant;
import java.util.List;

import org.apache.commons.lang3.mutable.MutableInt;

import net.denanu.wiki.content.PageContents;
import net.denanu.wiki.content.PageElement;
import net.denanu.wiki.gui.WikiScreen;
import net.denanu.wiki.gui.widgets.data.CraftingBackgrounds;
import net.denanu.wiki.gui.widgets.data.IngredientProvider;
import net.denanu.wiki.gui.widgets.data.SmithingReipeProviderFactory;
import net.denanu.wiki.uitls.SetUtils;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ScrollableWidget;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.CuttingRecipe;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.ShapedRecipe;
import net.minecraft.recipe.ShapelessRecipe;
import net.minecraft.recipe.SmithingRecipe;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.annotation.Debug;
import net.minecraft.util.collection.DefaultedList;

public class PageContentWidget extends ScrollableWidget {
	private final PageContents contents;
	private int contentHeight;
	private final ItemRenderer itemRenderer;
	private final WikiScreen screen;
	private int stackIdx;
	private boolean requiresNewLine = false;

	public PageContentWidget(final int x, final int y, final int w, final int h, final PageContents content, final ItemRenderer itemRenderer, final WikiScreen screen) {
		super(x, y, w, h, null);
		this.contents = content;
		this.contentHeight = 1;
		this.itemRenderer = itemRenderer;
		this.stackIdx = 0;
		this.screen = screen;
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
		return 16;
	}

	@Override
	protected void renderContents(final MatrixStack stack, final int mousex, final int mousey, final float delta) {
		final MutableInt x = new MutableInt(this.getX() + 4);
		final MutableInt y = new MutableInt(this.getY() + 4);

		for (final PageElement element : this.contents.getElements()) {
			element.getStyle().render(this, stack, x, y, this.width, element, mousex + 4, (int)(mousey + this.getScrollY() + 2));
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

		void render(final PageContentWidget page, final MatrixStack stack, final MutableInt x, final MutableInt y, final int width, final PageElement element, final int mousex, final int mousey) {
			this.renderer.render(page, stack, x, y, width, element, mousex, mousey);
		}

		private interface StyleRenderer{
			void render(PageContentWidget page, MatrixStack stack, MutableInt x, MutableInt y, int width, PageElement element, int mousex, int mousey);
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
		if (this.requiresNewLine) {
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

	private static void renderNormal(final PageContentWidget page, final MatrixStack stack, final MutableInt x, final MutableInt y, final int width, final PageElement element, final int mousex, final int mousey) {
		final TextRenderer renderer = PageContentWidget.getTextRenderer();

		PageContentWidget.renderTextBlock(renderer, stack, x, y, page.getLeft(), width, element);
		page.requiresNewLine = true;
	}

	private static Style.StyleRenderer renderSection(final float scale, final int topPadding) {
		return (final PageContentWidget page, final MatrixStack stack, final MutableInt x, final MutableInt y, final int width, final PageElement element, final int mousex, final int mousey) -> {
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
			page.requiresNewLine = true;
		};
	}

	private void renderItemTooltip(final MatrixStack stack, final int x, final int y, final int width, final int height, final int mousex, final int mousey, final ItemStack item) {
		if (this.hovered && x < mousex && mousex < x + width && y < mousey && mousey < y + height) {
			stack.push();
			stack.translate(mousex, mousey, 0);
			this.screen.renderTooltip(stack, item, 0, 0);
			stack.pop();
		}
	}

	private void renderItem(final MatrixStack stack, final ItemStack itm, final int x, final int y, final int mousex, final int mousey) {
		this.itemRenderer.renderGuiItemIcon(stack, itm, x, y);
		this.renderItemTooltip(stack, x, y, 18, 18, mousex, mousey, itm);
	}

	private void renderItem(final MatrixStack stack, final Item itm, final int x, final int y, final int mousex, final int mousey) {
		this.renderItem(stack, itm.getDefaultStack(), x, y, mousex, mousey);
	}

	private void renderItem(final MatrixStack stack, final ItemStack itm, final int x, final int y, final int xpos, final int ypos, final int mousex, final int mousey) {
		this.renderItem(stack, itm, xpos + 18*x, ypos + 18*y, mousex, mousey);
	}
	@Debug
	private void renderItem(final MatrixStack stack, final Item itm, final int x, final int y, final int xpos, final int ypos, final int mousex, final int mousey) {
		this.renderItem(stack, itm.getDefaultStack(), xpos + 18*x, ypos + 18*y, mousex, mousey, mousex, mousey);
	}

	private void renderIngredient(final MatrixStack stack, final Ingredient ingredient, final int x, final int y, final int mousex, final int mousey) {
		if (ingredient!= null && ingredient.getMatchingStacks().length > 0) {
			this.renderItem(stack, ingredient.getMatchingStacks()[this.stackIdx % ingredient.getMatchingStacks().length], x, y, mousex, mousey);
		}
	}

	private void renderIngredient(final MatrixStack stack, final Ingredient ingredient, final int x, final int y, final int xpos, final int ypos, final int mousex, final int mousey) {
		this.renderIngredient(stack, ingredient, xpos + 18*x, ypos + 18*y, mousex, mousey);
	}

	private void renderCrafters(final MatrixStack stack, final int x, int y, final List<Item> itms, final int mousex, final int mousey) {
		final Identifier PAGETAG = new Identifier("textures/gui/recipe_book.png");

		y += 3;

		final float scale = 0.65f;
		final int step = 20;
		final int xpos = (int) ((x - 2) / scale);
		final int ypos = (int) (y / scale);

		y += 2;
		int idx = 0;
		for (final Item itm : itms) {
			this.renderItem(stack, itm, x, y + idx*step, mousex, mousey);

			stack.push();
			stack.scale(scale, scale, 1f);
			this.drawTexture(stack, PAGETAG, xpos, (int)(ypos + idx * step / scale), 82, 208, 0, 32, 32, 256, 256);
			stack.pop();

			idx ++;
		}
	}

	public static void renderCrafting(final PageContentWidget page, final CraftingBackgrounds bg, final MatrixStack stack, final int posx, final int posy, final IngredientProvider ingredientProvider, final ItemStack output, final int mousex, final int mousey) {
		for (int idxX = 0; idxX < 3; idxX++) {
			for (int idxY = 0; idxY < 3; idxY++) {
				page.renderIngredient(stack, ingredientProvider.get(idxX + idxY * 3), idxX, idxY, posx + 10, posy + 10, mousex, mousey);
			}
		}
		page.renderItem(stack, output, posx + 104, posy + 28, mousex, mousey);
	}

	public static void renderSmithing(final PageContentWidget page, final CraftingBackgrounds bg, final MatrixStack stack, final int posx, final int posy, final IngredientProvider ingredientProvider, final ItemStack output, final int mousex, final int mousey) {
		final int inputY = 47;


		page.renderIngredient(stack, ingredientProvider.get(1), posx + 27, posy + inputY, mousex, mousey);
		page.renderIngredient(stack, ingredientProvider.get(2), posx + 76, posy + inputY + 1, mousex, mousey);

		page.renderItem(stack, output, posx + 134, posy + inputY, mousex, mousey);
	}

	public static void renderSmelting(final PageContentWidget page, final CraftingBackgrounds bg, final MatrixStack stack, final int posx, final int posy, final IngredientProvider ingredientProvider, final ItemStack output, final int mousex, final int mousey) {
		PageContentWidget.subRenderSmelting(page, stack, posx, posy, ingredientProvider, output, 11, 10, 70, 28, mousex, mousey);
		page.renderItem(stack, SetUtils.getMod(AbstractFurnaceBlockEntity.createFuelTimeMap().keySet(), page.stackIdx), 0, 0, posx + 11, posy + 46, mousex, mousey);
	}

	public static void renderCampfire(final PageContentWidget page, final CraftingBackgrounds bg, final MatrixStack stack, final int posx, final int posy, final IngredientProvider ingredientProvider, final ItemStack output, final int mousex, final int mousey) {
		PageContentWidget.subRenderSmelting(page, stack, posx, posy, ingredientProvider, output, 11, 13, 70, 14, mousex, mousey);
	}

	private static void subRenderSmelting(final PageContentWidget page, final MatrixStack stack, final int posx, final int posy, final IngredientProvider ingredientProvider, final ItemStack output, final int inputx, final int inputy, final int outputx, final int outputy, final int mousex, final int mousey) {
		page.renderIngredient(stack, ingredientProvider.get(0), posx + inputx, posy + inputy, mousex, mousey);
		page.renderItem(stack, output, 0, 0, posx + outputx, posy + outputy, mousex, mousey);
	}

	void renderBackdrop(final CraftingBackgrounds bg, final MatrixStack stack, final MutableInt x, final MutableInt y, final IngredientProvider ingredientProvider, final ItemStack output, final PageElement element, final int mousex, final int mousey) {
		this.newLine(y);
		this.setLeft(x);

		final int h = bg.h;
		final int w = bg.w;
		final int posx = (this.width - w) / 2 + this.getLeft();
		final int posy = y.intValue();

		bg.crafter.renderCrafting(this, bg, stack, posx, posy, ingredientProvider, output, mousex, mousey);

		this.renderCrafters(stack, posx + bg.w, posy, element.getCrafters(), mousex, mousey);
		stack.push();
		stack.translate(0, 0, 5);
		this.drawTexture(stack, bg.texture, posx, y.intValue(), 0, 0, 0, w, h, w, h);
		stack.pop();

		y.add(h + 5);
	}

	private static IngredientProvider getDefaultIngedientProvider(final Recipe<?> recipe) {
		return pos -> {
			final DefaultedList<Ingredient> ingreds = recipe.getIngredients();
			if (ingreds.size() > pos) {
				return ingreds.get(pos);
			}
			return null;
		};
	}

	private static void renderCrafting(final PageContentWidget page, final MatrixStack stack, final MutableInt x, final MutableInt y, final int width, final PageElement element, final int mousex, final int mousey) {
		final Recipe<?> recipe = element.getRecipe();
		if (recipe instanceof final CraftingRecipe crafting) {

			if (crafting instanceof final ShapedRecipe shape) {
				page.renderBackdrop(CraftingBackgrounds.CRAFTING, stack, x, y, PageContentWidget.getDefaultIngedientProvider(shape), shape.getOutput(null), element, mousex, mousey);
			}

			else if (crafting instanceof final ShapelessRecipe unshaped) {
				page.renderBackdrop(CraftingBackgrounds.CRAFTING, stack, x, y, PageContentWidget.getDefaultIngedientProvider(unshaped), unshaped.getOutput(null), element, mousex, mousey);
			}
		}

		else if (recipe instanceof final CuttingRecipe cutting) {
			page.renderBackdrop(CraftingBackgrounds.CAMPFIRE, stack, x, y, PageContentWidget.getDefaultIngedientProvider(cutting), cutting.getOutput(null), element, mousex, mousey);

		}

		else if (recipe instanceof final SmithingRecipe smithing) {
			page.renderBackdrop(CraftingBackgrounds.SMITHING, stack, x, y, SmithingReipeProviderFactory.build(smithing), smithing.getOutput(null), element, mousex, mousey);
		}
		page.requiresNewLine = false;
	}
}
