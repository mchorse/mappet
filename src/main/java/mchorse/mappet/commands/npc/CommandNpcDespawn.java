package mchorse.mappet.commands.npc;

import mchorse.mappet.entities.EntityNpc;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandNpcDespawn extends CommandNpcBase
{
    @Override
    public String getName()
    {
        return "despawn";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.npc.despawn";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}npc despawn{r} {7}<target>{r}";
    }

    @Override
    public int getRequiredArgs()
    {
        return 1;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        getEntity(server, sender, args[0], EntityNpc.class).setDead();
    }
}