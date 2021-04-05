package mchorse.mappet.commands;

import mchorse.mappet.commands.states.CommandState;
import net.minecraft.command.ICommandSender;

public class CommandMappet extends MappetSubCommandBase
{
    public CommandMappet()
    {
        this.add(new CommandState());
    }

    @Override
    public String getName()
    {
        return "mp";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.help";
    }
}
