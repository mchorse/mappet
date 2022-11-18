package mchorse.mappet.network.server.content;

import mchorse.mappet.api.utils.manager.IManager;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.content.PacketContentFolder;
import mchorse.mappet.network.common.content.PacketContentNames;
import mchorse.mappet.utils.CurrentSession;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class ServerHandlerContentFolder extends ServerMessageHandler<PacketContentFolder>
{

    @Override
    public void run(EntityPlayerMP player, PacketContentFolder message)
    {
        IManager manager = message.type.getManager();
        Path folder = manager.getFolder().toPath();
        if (message.rename != null && message.path.length() > 0)
        {
            folder.resolve(message.path).toFile().renameTo(folder.resolve(message.rename).toFile());
        }
        else if (message.delete && message.path.length() > 0)
        {
            folder.resolve(message.path).toFile().delete();
        }
        else
        {
            folder.resolve(message.path + message.name).toFile().mkdirs();
        }

        /* Synchronize names to players */
        List<String> names = new ArrayList<String>(message.type.getManager().getKeys());

        for (EntityPlayerMP otherPlayer : player.getServer().getPlayerList().getPlayers())
        {
            Dispatcher.sendTo(new PacketContentNames(message.type, names), otherPlayer);
        }
    }
}
