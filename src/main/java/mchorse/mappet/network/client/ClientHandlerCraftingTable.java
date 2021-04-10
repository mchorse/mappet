package mchorse.mappet.network.client;

import mchorse.mappet.client.gui.GuiCraftingTableScreen;
import mchorse.mappet.network.common.PacketCraftingTable;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerCraftingTable extends ClientMessageHandler<PacketCraftingTable>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketCraftingTable message)
    {
        Minecraft.getMinecraft().displayGuiScreen(new GuiCraftingTableScreen(message.table));
    }
}