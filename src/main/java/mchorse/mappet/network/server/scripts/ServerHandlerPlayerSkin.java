package mchorse.mappet.network.server.scripts;

import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.network.common.scripts.PacketClick;
import mchorse.mappet.network.common.scripts.PacketPlayerSkin;
import mchorse.mclib.network.ServerMessageHandler;
import net.minecraft.entity.player.EntityPlayerMP;

public class ServerHandlerPlayerSkin extends ServerMessageHandler<PacketPlayerSkin>
{
    @Override
    public void run(EntityPlayerMP player, PacketPlayerSkin message)
    {
        ICharacter character = Character.get(player);

        if (character.getSkin() == null)
        {
            character.setSkin(message.skin);
        }
    }
}