package mchorse.mappet.commands.factions;

import mchorse.mappet.api.states.States;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandFactionAdd extends CommandFactionBase
{
    @Override
    public String getName()
    {
        return "add";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.faction.add";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}faction add{r} {7}<target> <id> <expression>{r}";
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        States states = CommandFaction.getStates(server, sender, args[0]);
        String id = args[1];
        int value = CommandBase.parseInt(args[2]);
        double previous = states.getFactionScore(id);

        states.addFactionScore(id, value);

        this.getL10n().info(sender, "factions.changed", id, previous, states.getFactionScore(id));
    }
}