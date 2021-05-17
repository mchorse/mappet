package mchorse.mappet.network.client.content;

import mchorse.mappet.ClientProxy;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.client.gui.panels.GuiMappetDashboardPanel;
import mchorse.mappet.network.common.content.PacketContentNames;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerContentNames extends ClientMessageHandler<PacketContentNames>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketContentNames message)
    {
        if (message.requestId >= 0)
        {
            ClientProxy.process(message.names, message.requestId);

            return;
        }

        GuiMappetDashboard dashboard = GuiMappetDashboard.get(Minecraft.getMinecraft());
        GuiMappetDashboardPanel panel = message.type.get(dashboard);

        if (panel != null)
        {
            panel.fillNames(message.names);
        }
    }
}