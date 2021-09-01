package mchorse.mappet.network.client.blocks;

import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.network.common.blocks.PacketEditRegion;
import mchorse.mappet.tile.TileRegion;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerEditRegion extends ClientMessageHandler<PacketEditRegion>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketEditRegion message)
    {
        TileEntity tile = player.world.getTileEntity(message.pos);

        if (tile instanceof TileRegion)
        {
            TileRegion region = (TileRegion) tile;

            region.set(message.tag);

            if (message.open)
            {
                GuiMappetDashboard dashboard = GuiMappetDashboard.get(Minecraft.getMinecraft());

                dashboard.panels.setPanel(dashboard.region);
                dashboard.region.fill(region, true);

                Minecraft.getMinecraft().displayGuiScreen(dashboard);
            }
        }
    }
}