package mchorse.mappet.client.gui;

import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.client.gui.crafting.GuiCrafting;
import mchorse.mappet.client.gui.crafting.ICraftingScreen;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.crafting.PacketCraftingTable;
import mchorse.mclib.client.gui.framework.GuiBase;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;

public class GuiCraftingTableScreen extends GuiBase implements ICraftingScreen
{
    public GuiCrafting crafting;

    public GuiCraftingTableScreen(CraftingTable table)
    {
        super();

        Minecraft mc = Minecraft.getMinecraft();

        this.crafting = new GuiCrafting(mc);
        this.crafting.set(table);
        this.crafting.flex().relative(this.viewport).y(40).w(1F).h(1F, -40);

        this.root.add(this.crafting);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void refresh()
    {
        this.crafting.refresh();
    }

    @Override
    protected void closeScreen()
    {
        super.closeScreen();

        Dispatcher.sendToServer(new PacketCraftingTable(null));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();

        super.drawScreen(mouseX, mouseY, partialTicks);

        this.drawCenteredString(this.fontRenderer, this.crafting.get().title, this.viewport.mx(), 10, 0xffffff);
        this.fontRenderer.drawStringWithShadow(I18n.format("mappet.gui.interaction.crafting.recipe_title"), this.crafting.recipes.area.x + 4, this.crafting.recipes.area.y - 12, 0xffffff);
        this.fontRenderer.drawStringWithShadow(I18n.format("mappet.gui.crafting.input"), this.crafting.recipes.area.x(0.33F), this.crafting.recipes.area.y - 12, 0xffffff);
        this.fontRenderer.drawStringWithShadow(I18n.format("mappet.gui.crafting.output"), this.crafting.recipes.area.x(0.66F), this.crafting.recipes.area.y - 12, 0xffffff);
    }
}