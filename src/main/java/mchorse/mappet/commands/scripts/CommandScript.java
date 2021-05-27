package mchorse.mappet.commands.scripts;

import mchorse.mappet.commands.MappetSubCommandBase;
import net.minecraft.command.ICommandSender;

public class CommandScript extends MappetSubCommandBase
{
    public CommandScript()
    {
        this.add(new CommandScriptEval());
    }

    @Override
    public String getName()
    {
        return "script";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.script.help";
    }
}