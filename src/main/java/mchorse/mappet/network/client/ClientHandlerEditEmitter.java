package mchorse.mappet.network.client;

import mchorse.mappet.client.gui.GuiEmitterBlockScreen;
import mchorse.mappet.network.common.PacketEditEmitter;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerEditEmitter extends ClientMessageHandler<PacketEditEmitter>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketEditEmitter message)
    {
        Minecraft.getMinecraft().displayGuiScreen(new GuiEmitterBlockScreen(message.pos, message.expression, message.radius));
    }
}