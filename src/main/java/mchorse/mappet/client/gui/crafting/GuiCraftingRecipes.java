package mchorse.mappet.client.gui.crafting;

import mchorse.mappet.api.crafting.CraftingRecipe;
import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mclib.client.gui.framework.elements.GuiScrollElement;
import mchorse.mclib.client.gui.framework.elements.IGuiElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.utils.ColorUtils;
import net.minecraft.client.Minecraft;

import java.util.function.Consumer;

public class GuiCraftingRecipes extends GuiScrollElement
{
    private GuiCraftingRecipe current;

    private Consumer<GuiCraftingRecipe> callback;

    public GuiCraftingRecipes(Minecraft mc, Consumer<GuiCraftingRecipe> callback)
    {
        super(mc);

        this.callback = callback;

        this.flex().column(0).vertical().stretch().scroll();
    }

    public GuiCraftingRecipe getCurrent()
    {
        return this.current;
    }

    public void setTable(CraftingTable table)
    {
        this.removeAll();

        for (CraftingRecipe recipe : table.recipes)
        {
            GuiCraftingRecipe recipeElement = new GuiCraftingRecipe(this.mc, this, recipe);

            this.add(recipeElement);
        }

        if (this.getParentContainer() != null)
        {
            this.getParentContainer().resize();
        }
    }

    public void setRecipe(CraftingRecipe recipe)
    {
        for (IGuiElement element : this.getChildren())
        {
            GuiCraftingRecipe recipeElement = (GuiCraftingRecipe) element;

            if (recipeElement.getRecipe() == recipe)
            {
                this.current = recipeElement;
                this.scroll.scrollTo(this.current.area.y - this.area.y);

                break;
            }
        }
    }

    @Override
    public void draw(GuiContext context)
    {
        this.area.draw(ColorUtils.HALF_BLACK);

        super.draw(context);
    }

    public void recipeClicked(GuiCraftingRecipe recipe)
    {
        this.current = recipe;

        if (this.callback != null)
        {
            this.callback.accept(recipe);
        }
    }
}