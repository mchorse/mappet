package mchorse.mappet.commands.quests;

import mchorse.mappet.commands.MappetSubCommandBase;
import net.minecraft.command.ICommandSender;

public class CommandQuest extends MappetSubCommandBase
{
    public CommandQuest()
    {
        this.add(new CommandQuestAccept());
        this.add(new CommandQuestComplete());
        this.add(new CommandQuestDecline());
    }

    @Override
    public String getName()
    {
        return "quest";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.quest.help";
    }
}
