package mchorse.mappet.network.client;

import mchorse.mappet.client.gui.GuiCraftingTableScreen;
import mchorse.mappet.network.common.PacketCraft;
import mchorse.mclib.network.ClientMessageHandler;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerCraft extends ClientMessageHandler<PacketCraft>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketCraft message)
    {
        GuiScreen screen = Minecraft.getMinecraft().currentScreen;

        if (screen instanceof GuiCraftingTableScreen)
        {
            ((GuiCraftingTableScreen) screen).refresh();
        }
    }
}