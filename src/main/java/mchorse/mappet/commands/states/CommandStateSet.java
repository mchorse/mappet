package mchorse.mappet.commands.states;

import mchorse.mappet.api.states.States;
import mchorse.mclib.commands.SubCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandStateSet extends CommandStateBase
{
    @Override
    public String getName()
    {
        return "set";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.state.set";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}state set{r} {7}<target> <id> <value>{r}";
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        States states = CommandState.getStates(server, sender, args[0]);
        String id = args[1];

        try
        {
            states.setNumber(id, Double.parseDouble(args[2]));
        }
        catch (NumberFormatException e)
        {
            states.setString(id, String.join(" ", SubCommandBase.dropFirstArguments(args, 2)));
        }

        this.getL10n().info(sender, "states.set", id, states.values.get(id));
    }
}