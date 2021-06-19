package mchorse.mappet.commands.states;

import mchorse.mappet.api.states.States;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandStateAdd extends CommandStateBase
{
    @Override
    public String getName()
    {
        return "add";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.state.add";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}state add{r} {7}<target> <id> <number>{r}";
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        States states = CommandState.getStates(server, sender, args[0]);
        String id = args[1];
        double value = CommandBase.parseDouble(args[2]);
        double previous = states.getNumber(id);

        states.add(id, this.processValue(value));

        this.getL10n().info(sender, "states.changed", id, previous, states.getNumber(id));
    }

    protected double processValue(double value)
    {
        return value;
    }
}