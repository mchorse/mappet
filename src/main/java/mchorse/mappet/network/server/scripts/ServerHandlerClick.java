package mchorse.mappet.network.server.scripts;

import mchorse.mappet.Mappet;
import mchorse.mappet.network.common.scripts.PacketClick;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumHand;

public class ServerHandlerClick extends ServerMessageHandler<PacketClick>
{
    @Override
    public void run(EntityPlayerMP player, PacketClick message)
    {
        if (message.hand == EnumHand.MAIN_HAND && !Mappet.settings.playerLeftClick.isEmpty())
        {
            Mappet.settings.playerLeftClick.trigger(player);
        }
        else if (message.hand == EnumHand.OFF_HAND && !Mappet.settings.playerRightClick.isEmpty())
        {
            Mappet.settings.playerRightClick.trigger(player);
        }
    }
}