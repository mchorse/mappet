package mchorse.mappet.client.gui.crafting;

import mchorse.mappet.api.crafting.CraftingRecipe;
import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.crafting.PacketCraft;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiCrafting extends GuiElement implements ICraftingScreen
{
    public GuiCraftingRecipeList recipes;
    public GuiButtonElement craft;

    private CraftingTable table;

    public GuiCrafting(Minecraft mc)
    {
        super(mc);

        this.craft = new GuiButtonElement(mc, IKey.str("Craft!"), this::craft);
        this.craft.flex().relative(this.area).x(1F, -10).y(1F, -10).wh(80, 20).anchor(1F, 1F);

        this.recipes = new GuiCraftingRecipeList(mc, (list) -> this.pickRecipe(list.get(0)));
        this.recipes.background().flex().relative(this.area).x(10).y(10).w(1F, -20).hTo(this.craft.area, -5);

        this.add(this.craft, this.recipes);
    }

    public CraftingTable get()
    {
        return this.table;
    }

    public void set(CraftingTable table)
    {
        this.table = table;

        this.recipes.setList(this.table.recipes);
        this.recipes.setIndex(0);
        this.pickRecipe(this.table.recipes.get(0));
    }

    @Override
    public void refresh()
    {
        this.pickRecipe(this.recipes.getCurrentFirst());
    }

    private void craft(GuiButtonElement button)
    {
        Dispatcher.sendToServer(new PacketCraft(this.recipes.getIndex()));
    }

    private void pickRecipe(CraftingRecipe recipe)
    {
        this.craft.setEnabled(recipe.isPlayerHasAllItems(Minecraft.getMinecraft().player));
    }

    @Override
    public boolean keyTyped(GuiContext context)
    {
        if (super.keyTyped(context))
        {
            return true;
        }

        for (CraftingRecipe recipe : this.table.recipes)
        {
            if (recipe.hotkey > 0 && recipe.hotkey == context.keyCode)
            {
                this.pickRecipe(recipe);
                this.recipes.setCurrentScroll(recipe);
                this.craft(this.craft);

                return true;
            }
        }

        return false;
    }
}