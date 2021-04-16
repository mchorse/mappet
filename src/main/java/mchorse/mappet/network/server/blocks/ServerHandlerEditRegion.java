package mchorse.mappet.network.server.blocks;

import mchorse.mappet.network.common.blocks.PacketEditRegion;
import mchorse.mappet.network.common.blocks.PacketEditTrigger;
import mchorse.mappet.tile.TileRegion;
import mchorse.mappet.tile.TileTrigger;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class ServerHandlerEditRegion extends ServerMessageHandler<PacketEditRegion>
{
    @Override
    public void run(EntityPlayerMP player, PacketEditRegion message)
    {
        TileEntity tile = player.world.getTileEntity(message.pos);

        if (tile instanceof TileRegion)
        {
            ((TileRegion) tile).set(message.tag);
        }
    }
}