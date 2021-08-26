package mchorse.mappet.commands.morphs;

import mchorse.mappet.commands.MappetSubCommandBase;
import net.minecraft.command.ICommandSender;

public class CommandMorphAdd extends MappetSubCommandBase
{
    public CommandMorphAdd()
    {
        this.add(new CommandMorphAddEntity());
        this.add(new CommandMorphAddWorld());
    }

    @Override
    public String getName()
    {
        return "add";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.morph.add.help";
    }
}