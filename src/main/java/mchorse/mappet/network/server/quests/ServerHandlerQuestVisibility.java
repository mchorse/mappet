package mchorse.mappet.network.server.quests;

import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.network.common.quests.PacketQuestVisibility;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerHandlerQuestVisibility extends ServerMessageHandler<PacketQuestVisibility>
{
    @Override
    public void run(EntityPlayerMP player, PacketQuestVisibility message)
    {
        ICharacter character = Character.get(player);
        character.getQuests().getByName(message.id).visible = message.visible;
    }
}