package mchorse.mappet.network.server.dialogue;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.dialogues.DialogueFragment;
import mchorse.mappet.api.dialogues.DialogueNodeSystem;
import mchorse.mappet.api.dialogues.nodes.ReplyNode;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
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
            int i = message.index;

            DialogueNodeSystem dialogue = character.getDialogue();
            DialogueContext context = character.getDialogueContext();
            EventNode node = i >= 0 && i < context.replyNodes.size() ? context.replyNodes.get(message.index) : context.crafting;

            context.reset();
            Mappet.dialogues.recursiveExecute(dialogue, node, context, true);
            Mappet.dialogues.handleContext(player, dialogue, context);
        }
    }
}