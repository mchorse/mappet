package mchorse.mappet.commands.dialogues;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.dialogues.DialogueFragment;
import mchorse.mappet.api.dialogues.DialogueNodeSystem;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.dialogue.PacketDialogueFragment;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

import java.util.List;
import java.util.stream.Collectors;

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
        return "{l}{6}/{r}mp {8}dialogue open{r} {7}<player> <id>{r}";
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
        DialogueNodeSystem dialogue = this.getDialogue(id);

        if (dialogue.main == null)
        {
            throw new CommandException("dialogue.empty", id);
        }

        ICharacter character = Character.get(player);

        if (character != null)
        {
            DialogueContext context = new DialogueContext(server, player);

            character.setDialogue(dialogue, context);
            character.getStates().readDialogue(id);
            Mappet.dialogues.execute(dialogue, context);

            List<DialogueFragment> replies = context.replyNodes.stream().map((r) -> r.message).collect(Collectors.toList());

            Dispatcher.sendTo(new PacketDialogueFragment(dialogue.title, context.reactionNode.message, replies), player);
        }
    }
}
