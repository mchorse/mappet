package mchorse.mappet.network.client.npc;

import mchorse.mappet.entities.EntityNpc;
import mchorse.mappet.network.common.npc.PacketNpcStateChange;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerNpcStateChange extends ClientMessageHandler<PacketNpcStateChange>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketNpcStateChange message)
    {
        Entity entity = player.world.getEntityByID(message.id);

        if (entity instanceof EntityNpc)
        {
            ((EntityNpc) entity).setState(message.state, true);
        }
    }
}