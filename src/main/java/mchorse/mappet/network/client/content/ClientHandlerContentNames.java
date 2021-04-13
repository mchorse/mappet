package mchorse.mappet.network.client.content;

import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.panels.GuiMappetDashboardPanel;
import mchorse.mappet.network.common.content.PacketContentNames;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerContentNames extends ClientMessageHandler<PacketContentNames>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketContentNames message)
    {
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;

        if (screen instanceof GuiMappetDashboard)
        {
            GuiMappetDashboard dashboard = (GuiMappetDashboard) screen;
            GuiMappetDashboardPanel panel = message.type.get(dashboard);

            if (panel != null)
            {
                panel.fillNames(message.names);
            }
        }
    }
}