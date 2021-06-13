package mchorse.mappet.client.gui.nodes.events;

import mchorse.mappet.api.events.nodes.CancelNode;
import mchorse.mappet.client.gui.nodes.GuiEventBaseNodePanel;
import mchorse.mappet.client.gui.panels.GuiMappetDashboardPanel;
import net.minecraft.client.Minecraft;

public class GuiCancelNodePanel extends GuiEventBaseNodePanel<CancelNode>
{
    public GuiCancelNodePanel(Minecraft mc, GuiMappetDashboardPanel parentPanel)
    {
        super(mc);
    }
}