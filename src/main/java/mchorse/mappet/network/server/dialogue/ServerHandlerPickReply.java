package mchorse.mappet.network.server.dialogue;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.dialogues.DialogueFragment;
import mchorse.mappet.api.dialogues.DialogueNodeSystem;
import mchorse.mappet.api.dialogues.nodes.ReplyNode;
import mchorse.mappet.capabilities.Character;
import mchorse.mappet.capabilities.ICharacter;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.dialogue.PacketDialogueFragment;
import mchorse.mappet.network.common.dialogue.PacketPickReply;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;
import java.util.stream.Collectors;

public class ServerHandlerPickReply extends ServerMessageHandler<PacketPickReply>
{
    @Override
    public void run(EntityPlayerMP player, PacketPickReply message)
    {
        ICharacter character = Character.get(player);

        if (character != null && character.getDialogue() != null)
        {
            DialogueNodeSystem dialogue = character.getDialogue();
            ReplyNode reply = character.getDialogueContext().replyNodes.get(message.index);
            DialogueContext context = character.getDialogueContext();

            context.reactionNode = null;
            context.replyNodes.clear();
            Mappet.dialogues.recursiveExecute(dialogue, reply, context, true);

            List<DialogueFragment> replies = context.replyNodes.stream().map((r) -> r.message).collect(Collectors.toList());
            DialogueFragment reaction = context.reactionNode == null ? new DialogueFragment() : context.reactionNode.message;

            Dispatcher.sendTo(new PacketDialogueFragment(dialogue.title, reaction, replies), player);
        }
    }
}