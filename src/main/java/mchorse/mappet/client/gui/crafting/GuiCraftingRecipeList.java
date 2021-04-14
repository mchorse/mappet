package mchorse.mappet.client.gui.crafting;

import mchorse.mappet.api.crafting.CraftingRecipe;
import mchorse.mclib.client.gui.framework.elements.list.GuiListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiInventoryElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;

import java.util.List;
import java.util.function.Consumer;

public class GuiCraftingRecipeList extends GuiListElement<CraftingRecipe>
{
    public GuiCraftingRecipeList(Minecraft mc, Consumer<List<CraftingRecipe>> callback)
    {
        super(mc, callback);
    }

    @Override
    protected void drawElementPart(CraftingRecipe element, int i, int x, int y, boolean hover, boolean selected)
    {
        super.drawElementPart(element, i, x, y, hover, selected);

        if (this.area.w > 300)
        {
            RenderHelper.enableGUIStandardItemLighting();

            for (int j = 0; j < element.input.size(); j++)
            {
                ItemStack stack = element.input.get(j);

                GuiInventoryElement.drawItemStack(stack, this.area.x(0.33F) + 20 * j, y + this.scroll.scrollItemSize / 2 - 8, null);
            }

            for (int j = 0; j < element.output.size(); j++)
            {
                ItemStack stack = element.output.get(j);

                GuiInventoryElement.drawItemStack(stack, this.area.x(0.66F) + 20 * j, y + this.scroll.scrollItemSize / 2 - 8, null);
            }

            RenderHelper.disableStandardItemLighting();
        }
    }

    @Override
    protected String elementToString(CraftingRecipe element)
    {
        return element.title;
    }
}