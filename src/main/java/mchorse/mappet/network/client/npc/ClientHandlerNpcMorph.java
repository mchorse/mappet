package mchorse.mappet.network.client.npc;

import mchorse.mappet.entities.EntityNpc;
import mchorse.mappet.network.common.npc.PacketNpcMorph;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerNpcMorph extends ClientMessageHandler<PacketNpcMorph>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketNpcMorph message)
    {
        Entity entity = player.world.getEntityByID(message.id);

        if (entity instanceof EntityNpc)
        {
            ((EntityNpc) entity).setMorph(message.morph);
        }
    }
}