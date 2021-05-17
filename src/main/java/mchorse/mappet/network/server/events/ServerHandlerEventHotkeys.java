package mchorse.mappet.network.server.events;

import mchorse.mappet.Mappet;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.events.PacketEventHotkeys;
import mchorse.mappet.network.common.events.PacketEventPlayerHotkeys;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerHandlerEventHotkeys extends ServerMessageHandler<PacketEventHotkeys>
{
    @Override
    public void run(EntityPlayerMP player, PacketEventHotkeys message)
    {
        if (message.hotkeys != null)
        {
            Mappet.events.hotkeys.deserializeNBT(message.hotkeys);
            Mappet.events.hotkeys.save();

            for (EntityPlayerMP p : player.getServer().getPlayerList().getPlayers())
            {
                Dispatcher.sendTo(new PacketEventPlayerHotkeys(Mappet.events.hotkeys), p);
            }
        }
    }
}