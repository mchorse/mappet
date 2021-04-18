package mchorse.mappet.network.server.blocks;

import mchorse.mappet.network.common.blocks.PacketEditTrigger;
import mchorse.mappet.tile.TileTrigger;
import mchorse.mappet.utils.WorldUtils;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class ServerHandlerEditTrigger extends ServerMessageHandler<PacketEditTrigger>
{
    @Override
    public void run(EntityPlayerMP player, PacketEditTrigger message)
    {
        TileEntity tile = WorldUtils.getTileEntity(player.world, message.pos);

        if (tile instanceof TileTrigger)
        {
            ((TileTrigger) tile).set(message.left, message.right);
        }
    }
}