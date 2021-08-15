package mchorse.mappet.network.client.events;

import mchorse.mappet.client.KeyboardHandler;
import mchorse.mappet.network.common.events.PacketEventHotkeys;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerEventPlayerHotkeys extends ClientMessageHandler<PacketEventHotkeys>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketEventHotkeys message)
    {
        KeyboardHandler.hotkeys.clear();
        KeyboardHandler.hotkeys.addAll(message.hotkeys);
    }
}