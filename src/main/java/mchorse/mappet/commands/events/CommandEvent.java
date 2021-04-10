package mchorse.mappet.commands.events;

import mchorse.mappet.commands.MappetSubCommandBase;
import net.minecraft.command.ICommandSender;

public class CommandEvent extends MappetSubCommandBase
{
    public CommandEvent()
    {
        this.add(new CommandEventTrigger());
    }

    @Override
    public String getName()
    {
        return "event";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.event.help";
    }
}
