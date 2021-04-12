package mchorse.mappet.network.server;

import mchorse.mappet.network.common.PacketEditTrigger;
import mchorse.mappet.tile.TileTrigger;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class ServerHandlerEditTrigger extends ServerMessageHandler<PacketEditTrigger>
{
    @Override
    public void run(EntityPlayerMP player, PacketEditTrigger message)
    {
        TileEntity tile = player.world.getTileEntity(message.pos);

        if (tile instanceof TileTrigger)
        {
            ((TileTrigger) tile).set(message.left, message.right);
        }
    }
}