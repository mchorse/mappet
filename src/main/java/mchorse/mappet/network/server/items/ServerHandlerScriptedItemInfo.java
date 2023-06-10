package mchorse.mappet.network.server.items;

import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.items.PacketScriptedItemInfo;
import mchorse.mappet.utils.NBTUtils;
import mchorse.mclib.network.ServerMessageHandler;
import mchorse.mclib.utils.OpHelper;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ServerHandlerScriptedItemInfo extends ServerMessageHandler<PacketScriptedItemInfo>
{
    @Override
    public void run(EntityPlayerMP player, PacketScriptedItemInfo message)
    {
        if (!OpHelper.isPlayerOp(player))
        {
            return;
        }

        ItemStack stack = player.getHeldItemMainhand();

        if (NBTUtils.saveScriptedItemProps(stack, message.tag))
        {
            IMessage packet = new PacketScriptedItemInfo(message.tag, player.getEntityId());
            Dispatcher.sendTo(packet, player);
            Dispatcher.sendToTracked(player, packet);
        }
    }
}
