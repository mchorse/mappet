package mchorse.mappet.network.server.blocks;

import mchorse.mappet.network.common.blocks.PacketEditConditionModel;
import mchorse.mappet.tile.TileConditionModel;
import mchorse.mappet.utils.WorldUtils;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;

public class ServerHandlerEditConditionModel extends ServerMessageHandler<PacketEditConditionModel>
{
    @Override
    public void run(EntityPlayerMP player, PacketEditConditionModel message)
    {
        if (!player.isCreative())
        {
            return;
        }

        TileEntity tile = WorldUtils.getTileEntity(player.world, message.pos);

        if (tile instanceof TileConditionModel)
        {
            tile.readFromNBT(message.tag);
        }
    }
}