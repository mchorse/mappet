package mchorse.mappet.commands.data;

import mchorse.mappet.commands.MappetSubCommandBase;
import net.minecraft.command.ICommandSender;

public class CommandData extends MappetSubCommandBase
{
    public CommandData()
    {
        this.add(new CommandDataClear());
        this.add(new CommandDataLoad());
        this.add(new CommandDataRencode());
        this.add(new CommandDataSave());
    }

    @Override
    public String getName()
    {
        return "data";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.data.help";
    }
}