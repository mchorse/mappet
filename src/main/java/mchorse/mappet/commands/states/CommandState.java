package mchorse.mappet.commands.states;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.states.States;
import mchorse.mappet.commands.MappetSubCommandBase;
import mchorse.mappet.commands.factions.CommandFaction;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;

public class CommandState extends MappetSubCommandBase
{
    public static States getStates(MinecraftServer server, ICommandSender sender, String target) throws CommandException
    {
        if (target.equals("~"))
        {
            return Mappet.states;
        }

        return CommandFaction.getStates(server, sender, target);
    }

    public CommandState()
    {
        this.add(new CommandStateAdd());
        this.add(new CommandStateClear());
        this.add(new CommandStateIf());
        this.add(new CommandStatePrint());
        this.add(new CommandStateSet());
        this.add(new CommandStateSub());
    }

    @Override
    public String getName()
    {
        return "state";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.state.help";
    }
}
