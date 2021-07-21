package mchorse.mappet.network.client.content;

import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.network.common.content.PacketStates;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerStates extends ClientMessageHandler<PacketStates>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketStates message)
    {
        GuiMappetDashboard.get(Minecraft.getMinecraft()).settings.fillStates(message.target, message.states);
    }
}