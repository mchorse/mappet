package mchorse.mappet.network.server.crafting;

import mchorse.mappet.api.dialogues.nodes.ReactionNode;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.network.common.crafting.PacketCraftingTable;
import mchorse.mappet.utils.WorldUtils;
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

            if (character.getDialogueContext() != null)
            {
                ReactionNode node = character.getDialogueContext().reactionNode;

                if (node != null && !node.sound.isEmpty())
                {
                    WorldUtils.stopSound(player, node.sound);
                }

                character.setDialogue(null, null);
            }
        }
    }
}