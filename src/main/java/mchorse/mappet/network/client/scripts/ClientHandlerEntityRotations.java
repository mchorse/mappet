package mchorse.mappet.network.client.scripts;

import mchorse.mappet.network.common.scripts.PacketEntityRotations;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerEntityRotations extends ClientMessageHandler<PacketEntityRotations>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketEntityRotations message)
    {
        Entity entity = player.world.getEntityByID(message.entityId);

        if (entity != null)
        {
            entity.setLocationAndAngles(entity.posX, entity.posY, entity.posZ, message.yaw, message.pitch);
            entity.setRotationYawHead(message.yawHead);

            if (entity instanceof EntityLivingBase)
            {
                ((EntityLivingBase) entity).renderYawOffset = message.yawHead;
            }
        }
    }
}
