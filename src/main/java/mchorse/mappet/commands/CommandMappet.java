package mchorse.mappet.commands;

import mchorse.mappet.commands.crafting.CommandCrafting;
import mchorse.mappet.commands.data.CommandData;
import mchorse.mappet.commands.dialogues.CommandDialogue;
import mchorse.mappet.commands.events.CommandEvent;
import mchorse.mappet.commands.factions.CommandFaction;
import mchorse.mappet.commands.npc.CommandNpc;
import mchorse.mappet.commands.quests.CommandQuest;
import mchorse.mappet.commands.states.CommandState;
import net.minecraft.command.ICommandSender;

public class CommandMappet extends MappetSubCommandBase
{
    public CommandMappet()
    {
        this.add(new CommandCrafting());
        this.add(new CommandData());
        this.add(new CommandDialogue());
        this.add(new CommandEvent());
        this.add(new CommandFaction());
        this.add(new CommandNpc());
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

    @Override
    public int getRequiredPermissionLevel()
    {
        return 2;
    }
}
