package mchorse.mappet.network.client.quests;

import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.network.common.quests.PacketQuests;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerQuests extends ClientMessageHandler<PacketQuests>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketQuests message)
    {
        ICharacter character = Character.get(player);

        if (character != null)
        {
            character.getQuests().quests.clear();
            character.getQuests().quests.putAll(message.quests);
        }
    }
}