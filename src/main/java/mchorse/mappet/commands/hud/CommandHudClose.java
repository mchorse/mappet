package mchorse.mappet.commands.hud;

import mchorse.mappet.api.hud.HUDScene;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.hud.PacketHUDScene;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandHudClose extends CommandHudBase
{
    @Override
    public String getName()
    {
        return "close";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.hud.close";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}hud close{r} {7}<target> <id>{r}";
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        EntityPlayerMP player = getPlayer(server, sender, args[0]);

        Dispatcher.sendTo(new PacketHUDScene(args[1], null), player);
    }
}