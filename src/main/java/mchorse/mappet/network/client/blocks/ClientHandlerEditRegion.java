package mchorse.mappet.network.client.blocks;

import mchorse.mappet.network.common.blocks.PacketEditRegion;
import mchorse.mappet.tile.TileRegion;
import mchorse.mclib.network.ClientMessageHandler;
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
            ((TileRegion) tile).set(message.tag);
        }
    }
}