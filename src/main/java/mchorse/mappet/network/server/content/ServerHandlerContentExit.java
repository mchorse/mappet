package mchorse.mappet.network.server.content;

import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.content.PacketContentData;
import mchorse.mappet.network.common.content.PacketContentExit;
import mchorse.mappet.utils.CurrentSession;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class ServerHandlerContentExit extends ServerMessageHandler<PacketContentExit>
{
    public static void syncData(EntityPlayerMP except)
    {
        /* Before clearing current session, update the data for players that
         * are browsing this data */
        CurrentSession session = Character.get(except).getCurrentSession();
        NBTTagCompound data = null;
        int i = 0;

        if (session.type == null)
        {
            return;
        }

        for (EntityPlayerMP player : except.getServer().getPlayerList().getPlayers())
        {
            if (player == except)
            {
                continue;
            }

            CurrentSession otherSession = Character.get(player).getCurrentSession();

            if (otherSession.isActive(session.type, session.id))
            {
                if (data == null)
                {
                    data = session.type.getManager().load(session.id).serializeNBT();
                }

                PacketContentData packet = new PacketContentData(session.type, session.id, data);

                if (i > 0)
                {
                    packet.disallow();
                }

                Dispatcher.sendTo(packet, player);

                /* The first dog, gets the data editing privilege */
                if (i == 0)
                {
                    otherSession.set(session.type, session.id);
                }

                i += 1;
            }
        }
    }

    @Override
    public void run(EntityPlayerMP player, PacketContentExit message)
    {
        syncData(player);

        /* Clear current session */
        Character.get(player).getCurrentSession().reset();
    }
}