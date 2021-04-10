package mchorse.mappet.commands;

import mchorse.mappet.commands.crafting.CommandCrafting;
import mchorse.mappet.commands.dialogues.CommandDialogue;
import mchorse.mappet.commands.events.CommandEvent;
import mchorse.mappet.commands.quests.CommandQuest;
import mchorse.mappet.commands.states.CommandState;
import net.minecraft.command.ICommandSender;

public class CommandMappet extends MappetSubCommandBase
{
    public CommandMappet()
    {
        this.add(new CommandCrafting());
        this.add(new CommandDialogue());
        this.add(new CommandEvent());
        this.add(new CommandQuest());
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
