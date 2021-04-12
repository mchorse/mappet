package mchorse.mappet.network.client;

import mchorse.mappet.client.gui.GuiTriggerBlockScreen;
import mchorse.mappet.network.common.PacketEditTrigger;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerEditTrigger extends ClientMessageHandler<PacketEditTrigger>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketEditTrigger message)
    {
        Minecraft.getMinecraft().displayGuiScreen(new GuiTriggerBlockScreen(message.pos, message.left, message.right));
    }
}