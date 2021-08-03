package mchorse.mappet.network.client.ui;

import mchorse.mappet.client.gui.GuiUserInterface;
import mchorse.mappet.network.common.ui.PacketUI;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerUI extends ClientMessageHandler<PacketUI>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketUI message)
    {
        Minecraft mc = Minecraft.getMinecraft();

        mc.displayGuiScreen(new GuiUserInterface(mc, message.ui));
    }
}