package mchorse.mappet.network.server.npc;

import mchorse.mappet.Mappet;
import mchorse.mappet.network.common.npc.PacketNpcTool;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ServerHandlerNpcTool extends ServerMessageHandler<PacketNpcTool>
{
    @Override
    public void run(EntityPlayerMP player, PacketNpcTool message)
    {
        ItemStack stack = player.getHeldItemMainhand();

        if (stack.getItem() == Mappet.npcTool)
        {
            NBTTagCompound tag = stack.getTagCompound();

            if (tag == null)
            {
                tag = new NBTTagCompound();
                stack.setTagCompound(tag);
            }

            if (message.npc.isEmpty())
            {
                tag.removeTag("Npc");
            }
            else
            {
                tag.setString("Npc", message.npc);
            }

            if (message.state.isEmpty())
            {
                tag.removeTag("State");
            }
            else
            {
                tag.setString("State", message.state);
            }
        }
    }
}