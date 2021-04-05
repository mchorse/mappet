package mchorse.mappet.commands.states;

import mchorse.mappet.commands.MappetSubCommandBase;
import net.minecraft.command.ICommandSender;

public class CommandState extends MappetSubCommandBase
{
    public CommandState()
    {
        this.add(new CommandStateAdd());
        this.add(new CommandStateIf());
        this.add(new CommandStateReset());
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
