package mchorse.mappet.network.server.content;

import mchorse.mappet.api.states.States;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.content.PacketRequestStates;
import mchorse.mappet.network.common.content.PacketStates;
import mchorse.mclib.network.ServerMessageHandler;
import mchorse.mclib.utils.OpHelper;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerHandlerRequestStates extends ServerMessageHandler<PacketRequestStates>
{
    @Override
    public void run(EntityPlayerMP player, PacketRequestStates message)
    {
        if (!OpHelper.isPlayerOp(player))
        {
            return;
        }

        States states = ServerHandlerStates.getStates(player.world.getMinecraftServer(), message.target);

        if (states != null)
        {
            Dispatcher.sendTo(new PacketStates(message.target, states.serializeNBT()), player);
        }
    }
}