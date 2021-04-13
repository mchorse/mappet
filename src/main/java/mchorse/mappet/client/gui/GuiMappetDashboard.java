package mchorse.mappet.client.gui;

import mchorse.mappet.client.gui.panels.GuiCraftingTablePanel;
import mchorse.mclib.client.gui.mclib.GuiAbstractDashboard;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanels;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiMappetDashboard extends GuiAbstractDashboard
{
    public GuiCraftingTablePanel crafting;

    public GuiMappetDashboard(Minecraft mc)
    {
        super(mc);
    }

    @Override
    protected GuiDashboardPanels createDashboardPanels(Minecraft mc)
    {
        return new GuiDashboardPanels(mc);
    }

    @Override
    protected void registerPanels(Minecraft mc)
    {
        this.crafting = new GuiCraftingTablePanel(mc, this);

        this.panels.registerPanel(this.crafting, IKey.lang("mappet.gui.panels.crafting"), Icons.WRENCH);

        this.panels.setPanel(this.crafting);
    }
}