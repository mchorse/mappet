package mchorse.mappet.network.server.blocks;

import mchorse.mappet.network.common.blocks.PacketEditEmitter;
import mchorse.mappet.tile.TileEmitter;
import mchorse.mappet.utils.WorldUtils;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class ServerHandlerEditEmitter extends ServerMessageHandler<PacketEditEmitter>
{
    @Override
    public void run(EntityPlayerMP player, PacketEditEmitter message)
    {
        if (!player.isCreative())
        {
            return;
        }

        TileEntity tile = WorldUtils.getTileEntity(player.world, message.pos);

        if (tile instanceof TileEmitter)
        {
            ((TileEmitter) tile).setExpression(message);
        }
    }
}