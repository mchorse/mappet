package mchorse.mappet.network.server;

import mchorse.mappet.capabilities.Character;
import mchorse.mappet.capabilities.ICharacter;
import mchorse.mappet.network.common.PacketCraftingTable;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerHandlerCraftingTable extends ServerMessageHandler<PacketCraftingTable>
{
    @Override
    public void run(EntityPlayerMP player, PacketCraftingTable message)
    {
        ICharacter character = Character.get(player);

        if (character != null)
        {
            character.setCraftingTable(null);
        }
    }
}