package mchorse.mappet.network.server.quests;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.api.quests.chains.QuestStatus;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.network.common.quests.PacketQuestAction;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerHandlerQuestAction extends ServerMessageHandler<PacketQuestAction>
{
    @Override
    public void run(EntityPlayerMP player, PacketQuestAction message)
    {
        ICharacter character = Character.get(player);

        if (message.status == QuestStatus.AVAILABLE)
        {
            Quest quest = Mappet.quests.load(message.id);

            if (quest != null)
            {
                character.getQuests().add(quest, player);
            }
        }
        else if (message.status == QuestStatus.COMPLETED)
        {
            Quest quest = character.getQuests().getByName(message.id);

            if (quest != null && quest.isComplete(player))
            {
                character.getQuests().complete(message.id, player);

                /* Update quests, because there might be some new quests down the chain */
                Mappet.dialogues.handleContext(player, character.getDialogue(), character.getDialogueContext(), null);
            }
        }
        else if (message.status == QuestStatus.CANCELED)
        {
            Quest quest = character.getQuests().getByName(message.id);

            if (quest != null && quest.cancelable)
            {
                character.getQuests().remove(message.id, player, false);
            }
        }
    }
}