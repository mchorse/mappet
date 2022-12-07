package mchorse.mappet.network.server.ui;

import mchorse.mappet.api.ui.UIContext;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.network.common.ui.PacketUI;
import mchorse.mappet.network.common.ui.PacketUIData;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerHandlerUI extends ServerMessageHandler<PacketUI>
{
    @Override
    public void run(EntityPlayerMP player, PacketUI message)
    {
        ICharacter character = Character.get(player);
        UIContext context = character.getUIContext();

        if (context != null && context.ui.getUIId().equals(message.ui.getUIId()))
        {
            context.close();
            character.setUIContext(null);
        }
    }
}