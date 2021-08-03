package mchorse.mappet.network.server.ui;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.network.common.ui.PacketUIData;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerHandlerUIData extends ServerMessageHandler<PacketUIData>
{
    @Override
    public void run(EntityPlayerMP player, PacketUIData message)
    {
        ICharacter character = Character.get(player);
        UIContext context = character.getUIContext();

        if (context == null)
        {
            return;
        }

        context.handleNewData(message.data);
    }
}