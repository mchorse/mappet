package mchorse.mappet.network.client.scripts;

import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.network.common.scripts.PacketRepl;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerRepl extends ClientMessageHandler<PacketRepl>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketRepl message)
    {
        GuiMappetDashboard.get(Minecraft.getMinecraft()).script.repl.log(message.code);
    }
}