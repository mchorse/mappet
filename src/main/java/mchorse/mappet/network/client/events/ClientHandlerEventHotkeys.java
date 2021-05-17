package mchorse.mappet.network.client.events;

import mchorse.mappet.api.events.hotkeys.EventHotkeys;
import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mappet.network.common.events.PacketEventHotkeys;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerEventHotkeys extends ClientMessageHandler<PacketEventHotkeys>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketEventHotkeys message)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.currentScreen instanceof GuiMappetDashboard)
        {
            EventHotkeys hotkeys = new EventHotkeys();

            hotkeys.deserializeNBT(message.hotkeys);

            ((GuiMappetDashboard) mc.currentScreen).event.openHotkeysEditor(hotkeys);
        }
    }
}