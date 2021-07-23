package mchorse.mappet.client.gui.crafting;

import mchorse.mappet.api.crafting.CraftingRecipe;
import mchorse.mappet.client.gui.utils.text.GuiText;
import mchorse.mappet.utils.MPIcons;
import mchorse.mclib.McLib;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiSlotElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.ColorUtils;
import mchorse.mclib.utils.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class GuiCraftingRecipe extends GuiElement
{
    private CraftingRecipe recipe;
    private GuiCraftingRecipes recipes;

    public GuiCraftingRecipe(Minecraft mc, GuiCraftingRecipes recipes, CraftingRecipe recipe)
    {
        super(mc);

        this.recipe = recipe;
        this.recipes = recipes;

        GuiIconElement in = new GuiIconElement(mc, MPIcons.IN, null);
        GuiIconElement out = new GuiIconElement(mc, MPIcons.OUT, null);

        in.setEnabled(false);
        in.disabledColor = 0xffffffff;
        out.setEnabled(false);
        out.disabledColor = 0xffffffff;

        GuiElement output = this.createItems(mc, recipe.output);
        GuiElement column = Elements.column(mc, 4, Elements.label(IKey.str(TextUtils.processColoredText(recipe.title))));

        if (!recipe.description.trim().isEmpty())
        {
            column.add(new GuiText(mc).text(TextUtils.processColoredText(recipe.description)).color(0xaaaaaa, true).marginTop(4));
        }

        column.add(Elements.label(IKey.lang("mappet.gui.crafting.input")).marginTop(12), this.createItems(mc, recipe.input));
        output.flex().w(recipe.output.size() > 1 ? 44 : 20);

        this.add(Elements.row(mc, 5, column, output));

        this.flex().column(4).vertical().stretch().padding(10);
    }

    private GuiElement createItems(Minecraft mc, NonNullList<ItemStack> input)
    {
        GuiElement element = new GuiElement(mc);

        for (ItemStack stack : input)
        {
            GuiSlotElement slot = new GuiSlotElement(mc, 0, null);

            slot.drawDisabled = false;
            slot.setStack(stack);
            slot.setEnabled(false);
            slot.flex().wh(20, 20);

            element.add(slot);
        }

        element.flex().grid(4).width(20);

        return element;
    }

    public CraftingRecipe getRecipe()
    {
        return this.recipe;
    }

    @Override
    public boolean mouseClicked(GuiContext context)
    {
        if (super.mouseClicked(context))
        {
            return true;
        }

        if (this.area.isInside(context) && context.mouseButton == 0)
        {
            this.recipes.recipeClicked(this);

            return true;
        }

        return false;
    }

    @Override
    public void draw(GuiContext context)
    {
        if (this.recipes.getCurrent() == this)
        {
            this.area.draw(0x88000000 + McLib.primaryColor.get());
        }

        int y = this.area.ey();

        Gui.drawRect(this.area.x, y - 1, this.area.ex(), y, ColorUtils.HALF_BLACK);

        super.draw(context);
    }
}