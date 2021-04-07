package mchorse.mappet.network.server;

import mchorse.mappet.capabilities.Character;
import mchorse.mappet.capabilities.ICharacter;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.PacketCraft;
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
            character.getCraftingTable().recipes.get(message.index).craft(player);

            Dispatcher.sendTo(message, player);
        }
    }
}