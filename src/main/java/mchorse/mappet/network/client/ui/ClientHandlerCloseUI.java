package mchorse.mappet.network.client.ui;

import mchorse.mappet.network.common.ui.PacketCloseUI;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerCloseUI extends ClientMessageHandler<PacketCloseUI>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketCloseUI message)
    {
        Minecraft mc = Minecraft.getMinecraft();

        /* Just in case there will server side grievers who will spam the
         * close UI packets to some players. This check should at least allow
         * players to leave that server/world */
        if (!(mc.currentScreen instanceof GuiIngameMenu))
        {
            mc.displayGuiScreen(null);
        }
    }
}