package mchorse.mappet.network.server.content;

import mchorse.mappet.api.utils.IManager;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.content.PacketContentData;
import mchorse.mappet.network.common.content.PacketContentNames;
import mchorse.mappet.utils.CurrentSession;
import mchorse.mclib.network.ServerMessageHandler;
import mchorse.mclib.utils.OpHelper;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.List;

public class ServerHandlerContentData extends ServerMessageHandler<PacketContentData>
{
    @Override
    public void run(EntityPlayerMP player, PacketContentData message)
    {
        boolean isEditing = !Character.get(player).getCurrentSession().isEditing(message.type, message.name);
        boolean exists = message.type.getManager().exists(message.name);

        if (!OpHelper.isPlayerOp(player) || (isEditing && exists))
        {
            return;
        }

        IManager manager = message.type.getManager();

        if (message.rename != null)
        {
            manager.rename(message.name, message.rename);

            if (message.data != null)
            {
                manager.save(message.rename, message.data);
            }
        }
        else if (message.data == null)
        {
            manager.delete(message.name);
        }
        else
        {
            manager.save(message.name, message.data);
        }

        if (!exists && manager.exists(message.name))
        {
            CurrentSession session = Character.get(player).getCurrentSession();

            session.set(message.type, message.name);
            session.setActive(message.type, message.name);
        }

        /* Synchronize names to other players */
        List<String> names = new ArrayList<String>(message.type.getManager().getKeys());

        for (EntityPlayerMP otherPlayer : player.getServer().getPlayerList().getPlayers())
        {
            if (otherPlayer == player)
            {
                continue;
            }

            CurrentSession session = Character.get(otherPlayer).getCurrentSession();

            Dispatcher.sendTo(new PacketContentNames(message.type, names), otherPlayer);

            if (session.isActive(message.type, message.name))
            {
                Dispatcher.sendTo(message.disallow(), otherPlayer);
            }
        }
    }
}