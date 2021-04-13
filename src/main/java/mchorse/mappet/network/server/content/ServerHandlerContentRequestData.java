package mchorse.mappet.network.server.content;

import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.content.PacketContentData;
import mchorse.mappet.network.common.content.PacketContentRequestData;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class ServerHandlerContentRequestData extends ServerMessageHandler<PacketContentRequestData>
{
    @Override
    public void run(EntityPlayerMP player, PacketContentRequestData message)
    {
        NBTTagCompound tag = message.type.getManager().load(message.name).serializeNBT();

        Dispatcher.sendTo(new PacketContentData(message.type, message.name, tag), player);
    }
}
