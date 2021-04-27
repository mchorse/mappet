package mchorse.mappet.client.gui;

import mchorse.mappet.api.crafting.CraftingRecipe;
import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.client.gui.crafting.GuiCraftingRecipeList;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.crafting.PacketCraft;
import mchorse.mappet.network.common.crafting.PacketCraftingTable;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

import java.io.IOException;

public class GuiCraftingTableScreen extends GuiBase
{
    public GuiCraftingRecipeList recipes;
    public GuiButtonElement craft;

    private CraftingTable table;

    public GuiCraftingTableScreen(CraftingTable table)
    {
        super();

        this.table = table;

        Minecraft mc = Minecraft.getMinecraft();

        this.craft = new GuiButtonElement(mc, IKey.str("Craft!"), this::craft);
        this.craft.flex().relative(this.viewport).x(1F, -10).y(1F, -10).wh(80, 20).anchor(1F, 1F);

        this.recipes = new GuiCraftingRecipeList(mc, (list) -> this.pickRecipe(list.get(0)));
        this.recipes.setList(this.table.recipes);
        this.recipes.background().flex().relative(this.viewport).x(10).y(50).w(1F, -20).hTo(this.craft.area, -5);

        this.root.add(this.craft, this.recipes);

        this.recipes.setIndex(0);
        this.pickRecipe(this.table.recipes.get(0));
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

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
    protected void closeScreen()
    {
        super.closeScreen();

        Dispatcher.sendToServer(new PacketCraftingTable(null));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        for (CraftingRecipe recipe : this.table.recipes)
        {
            if (recipe.hotkey > 0 && recipe.hotkey == keyCode)
            {
                this.pickRecipe(recipe);
                this.recipes.setCurrentScroll(recipe);
                this.craft(this.craft);

                return;
            }
        }

        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);

        this.drawCenteredString(this.fontRenderer, this.table.title, this.viewport.mx(), 10, 0xffffff);
        this.fontRenderer.drawStringWithShadow("Title", this.recipes.area.x + 4, this.recipes.area.y - 12, 0xffffff);
        this.fontRenderer.drawStringWithShadow("Ingredients", this.recipes.area.x(0.33F), this.recipes.area.y - 12, 0xffffff);
        this.fontRenderer.drawStringWithShadow("Result", this.recipes.area.x(0.66F), this.recipes.area.y - 12, 0xffffff);
    }
}