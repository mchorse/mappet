package mchorse.mappet.commands.quests;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.commands.MappetCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public abstract class CommandQuestBase extends MappetCommandBase
{
    protected Quest getQuest(String id) throws CommandException
    {
        Quest quest = Mappet.quests.load(id);

        if (quest == null)
        {
            throw new CommandException("quest.missing", id);
        }

        return quest;
    }

    @Override
    public int getRequiredArgs()
    {
        return 2;
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, Mappet.quests.getKeys());
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}