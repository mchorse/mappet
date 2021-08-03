package mchorse.mappet.network.server.ui;

import mchorse.mappet.network.common.ui.PacketUIData;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerHandlerUIData extends ServerMessageHandler<PacketUIData>
{
    @Override
    public void run(EntityPlayerMP player, PacketUIData message)
    {
        System.out.println(message.data.toString());
    }
}