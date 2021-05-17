package mchorse.mappet.network.server.events;

import mchorse.mappet.Mappet;
import mchorse.mappet.network.common.events.PacketEventHotkey;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerHandlerEventHotkey extends ServerMessageHandler<PacketEventHotkey>
{
    @Override
    public void run(EntityPlayerMP player, PacketEventHotkey message)
    {
        Mappet.events.hotkeys.execute(player, message.keycode);
    }
}