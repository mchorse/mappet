package mchorse.mappet.network.client.items;

import mchorse.mappet.common.ScriptedItemProps;
import mchorse.mappet.network.common.items.PacketScriptedItemInfo;
import mchorse.mappet.utils.NBTUtils;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;


public class ClientHandlerScriptedItemInfo extends ClientMessageHandler<PacketScriptedItemInfo> {
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketScriptedItemInfo message) {
        Entity entity = player.world.getEntityByID(message.entity);

        if (entity instanceof EntityLivingBase)
        {
            EntityLivingBase base = (EntityLivingBase) entity;
            ItemStack stack = base.getHeldItemMainhand();

            if (stack.isEmpty())
            {
                return;
            }

            ScriptedItemProps newProps = new ScriptedItemProps(message.tag);
            if (newProps.hasChanged()) {
                NBTUtils.saveScriptedItemProps(stack, message.tag);
            }
        }
    }
}
