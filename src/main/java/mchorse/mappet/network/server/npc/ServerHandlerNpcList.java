package mchorse.mappet.network.server.npc;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.npc.PacketNpcList;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.Collections;

public class ServerHandlerNpcList extends ServerMessageHandler<PacketNpcList>
{
    @Override
    public void run(EntityPlayerMP player, PacketNpcList message)
    {
        if (message.npcs.isEmpty())
        {
            return;
        }

        Npc npc = Mappet.npcs.load(message.npcs.get(0));

        if (npc != null)
        {
            Dispatcher.sendTo(new PacketNpcList(Collections.emptyList(), npc.states.keySet()).states(), player);
        }
    }
}