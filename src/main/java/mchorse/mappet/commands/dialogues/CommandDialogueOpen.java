package mchorse.mappet.commands.dialogues;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.dialogues.Dialogue;
import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

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
        return "{l}{6}/{r}mp {8}dialogue open{r} {7}<player> <id> [subject_id]{r}";
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

        DialogueContext context = new DialogueContext(new DataContext(player));

        if (args.length > 2)
        {
            context.data.set("subject_id", args[2]);
        }

        Mappet.dialogues.open(player, dialogue, context);
    }
}
