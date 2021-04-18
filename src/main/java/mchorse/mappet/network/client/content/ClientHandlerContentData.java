package mchorse.mappet.network.client.content;

import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.panels.GuiMappetDashboardPanel;
import mchorse.mappet.network.common.content.PacketContentData;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerContentData extends ClientMessageHandler<PacketContentData>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketContentData message)
    {
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;

        if (screen instanceof GuiMappetDashboard)
        {
            GuiMappetDashboard dashboard = (GuiMappetDashboard) screen;
            GuiMappetDashboardPanel panel = message.type.get(dashboard);

            if (panel != null)
            {
                panel.fill(message.name, message.type.getManager().create(message.data));
            }
        }
    }
}