package mchorse.mappet.commands.dialogues;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.dialogues.Dialogue;
import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.commands.CommandMappet;
import mchorse.mclib.commands.SubCommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;
import java.util.List;

public class CommandDialogueOpen extends CommandDialogueBase
{
    @Override
    public String getName()
    {
        return "open";
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "mappet.commands.mp.dialogue.open";
    }

    @Override
    public String getSyntax()
    {
        return "{l}{6}/{r}mp {8}dialogue open{r} {7}<player> <id> [data]{r}";
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    @Override
    public void executeCommand(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        String id = args[1];
        EntityPlayerMP player = getPlayer(server, sender, args[0]);
        Dialogue dialogue = this.getDialogue(id);

        if (dialogue.main == null)
        {
            throw new CommandException("dialogue.empty", id);
        }

        DataContext context = new DataContext(player);

        if (args.length > 2)
        {
            context.parse(String.join(" ", SubCommandBase.dropFirstArguments(args, 2)));
        }

        Mappet.dialogues.open(player, dialogue, new DialogueContext(context));
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos)
    {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, CommandMappet.listOfPlayers(server));
        }

        return super.getTabCompletions(server, sender, args, targetPos);
    }
}
