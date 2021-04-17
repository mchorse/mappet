package mchorse.mappet.client.gui.panels;

import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import net.minecraft.client.Minecraft;

public class GuiNpcPanel extends GuiMappetDashboardPanel
{
    public GuiNpcPanel(Minecraft mc, GuiMappetDashboard dashboard)
    {
        super(mc, dashboard);
    }

    @Override
    public ContentType getType()
    {
        return null;
    }

    @Override
    public String getTitle()
    {
        return null;
    }
}