package mchorse.mappet.network.server.content;

import mchorse.mappet.api.utils.IManager;
import mchorse.mappet.network.common.content.PacketContentData;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerHandlerContentData extends ServerMessageHandler<PacketContentData>
{
    @Override
    public void run(EntityPlayerMP player, PacketContentData message)
    {
        IManager manager = message.type.getManager();

        if (message.rename != null)
        {
            manager.rename(message.name, message.rename);
        }
        else if (message.data == null)
        {
            manager.delete(message.name);
        }
        else
        {
            manager.save(message.name, message.data);
        }
    }
}