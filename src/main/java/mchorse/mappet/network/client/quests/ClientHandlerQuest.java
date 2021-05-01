package mchorse.mappet.network.client.quests;

import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.network.common.quests.PacketQuest;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerQuest extends ClientMessageHandler<PacketQuest>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketQuest message)
    {
        ICharacter character = Character.get(player);

        if (character != null)
        {
            if (message.quest == null)
            {
                character.getQuests().quests.remove(message.id);
            }
            else
            {
                character.getQuests().quests.put(message.id, message.quest);
            }
        }
    }
}