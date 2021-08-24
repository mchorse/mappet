package mchorse.mappet.commands.morphs;

import mchorse.mappet.commands.MappetSubCommandBase;
import net.minecraft.command.ICommandSender;

public class CommandMorph extends MappetSubCommandBase
{
    public CommandMorph()
    {
        this.add(new CommandMorphAdd());
    }

    @Override
    public String getName()
    {
        return "morph";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.morph.help";
    }
}