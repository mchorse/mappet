package mchorse.mappet.network.server.content;

import mchorse.mappet.api.utils.manager.IManager;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.content.PacketContentFolder;
import mchorse.mappet.network.common.content.PacketContentNames;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

import java.io.File;
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

        if (message.rename != null && !message.path.isEmpty())
        {
            int lastIndex = message.path.lastIndexOf('/');
            String newPath = lastIndex == -1 ? "" + message.rename : message.path.substring(0, lastIndex + 1) + message.rename;

            folder.resolve(message.path).toFile().renameTo(folder.resolve(newPath).toFile());
        }
        else if (message.delete && !message.path.isEmpty())
        {
            File deleteFolder = folder.resolve(message.path).toFile();

            for (File file : deleteFolder.listFiles())
            {
                file.delete();
            }

            deleteFolder.delete();
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
