package mchorse.mappet.client.gui.nodes.events;

import mchorse.mappet.api.events.nodes.CancelNode;
import mchorse.mappet.client.gui.nodes.GuiEventBaseNodePanel;
import net.minecraft.client.Minecraft;

public class GuiCancelNodePanel extends GuiEventBaseNodePanel<CancelNode>
{
    public GuiCancelNodePanel(Minecraft mc)
    {
        super(mc);
    }
}