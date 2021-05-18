package mchorse.mappet.network.server.crafting;

import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.crafting.PacketCraft;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerHandlerCraft extends ServerMessageHandler<PacketCraft>
{
    @Override
    public void run(EntityPlayerMP player, PacketCraft message)
    {
        ICharacter character = Character.get(player);

        if (character != null && character.getCraftingTable() != null)
        {
            DataContext context = null;

            if (character.getDialogueContext() != null)
            {
                context = character.getDialogueContext().data;
            }

            character.getCraftingTable().recipes.get(message.index).craft(player, context);

            Dispatcher.sendTo(message, player);
        }
    }
}