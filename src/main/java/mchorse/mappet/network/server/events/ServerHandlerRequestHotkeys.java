package mchorse.mappet.network.server.events;

import mchorse.mappet.Mappet;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.events.PacketEventHotkeys;
import mchorse.mappet.network.common.events.PacketEventRequestHotkeys;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerHandlerRequestHotkeys extends ServerMessageHandler<PacketEventRequestHotkeys>
{
    @Override
    public void run(EntityPlayerMP player, PacketEventRequestHotkeys message)
    {
        Dispatcher.sendTo(new PacketEventHotkeys(Mappet.events.hotkeys.serializeNBT()), player);
    }
}