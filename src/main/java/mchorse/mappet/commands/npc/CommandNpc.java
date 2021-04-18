package mchorse.mappet.commands.npc;

import mchorse.mappet.commands.MappetSubCommandBase;
import net.minecraft.command.ICommandSender;

public class CommandNpc extends MappetSubCommandBase
{
    public CommandNpc()
    {
        this.add(new CommandNpcState());
        this.add(new CommandNpcSummon());
    }

    @Override
    public String getName()
    {
        return "npc";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.npc.help";
    }
}