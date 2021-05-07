package mchorse.mappet.commands.dialogues;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.dialogues.Dialogue;
import mchorse.mappet.commands.MappetCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public abstract class CommandDialogueBase extends MappetCommandBase
{
    protected Dialogue getDialogue(String id) throws CommandException
    {
        Dialogue dialogue = Mappet.dialogues.load(id);

        if (dialogue == null)
        {
            throw new CommandException("dialogue.missing", id);
        }

        return dialogue;
    }

    @Override
    public int getRequiredArgs()
    {
        return 2;
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 2)
        {
            return getListOfStringsMatchingLastWord(args, Mappet.dialogues.getKeys());
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}