package mchorse.mappet.network.client.ui;

import mchorse.mappet.client.gui.GuiUserInterface;
import mchorse.mappet.network.common.ui.PacketUIData;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerUIData extends ClientMessageHandler<PacketUIData>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketUIData message)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.currentScreen instanceof GuiUserInterface)
        {
            GuiUserInterface screen = (GuiUserInterface) mc.currentScreen;

            screen.handleUIChanges(message.data);
        }
    }
}