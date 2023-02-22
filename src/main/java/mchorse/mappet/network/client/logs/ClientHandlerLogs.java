package mchorse.mappet.network.client.logs;

import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.network.common.logs.PacketLogs;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerLogs extends ClientMessageHandler<PacketLogs>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketLogs message)
    {
        GuiMappetDashboard.get(Minecraft.getMinecraft()).logs.update(message.text);
    }
}