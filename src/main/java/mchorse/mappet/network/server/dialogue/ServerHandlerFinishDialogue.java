package mchorse.mappet.network.server.dialogue;

import mchorse.mappet.api.dialogues.nodes.ReactionNode;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.network.common.dialogue.PacketFinishDialogue;
import mchorse.mappet.utils.WorldUtils;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ServerHandlerFinishDialogue extends ServerMessageHandler<PacketFinishDialogue>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerMP player, PacketFinishDialogue message)
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