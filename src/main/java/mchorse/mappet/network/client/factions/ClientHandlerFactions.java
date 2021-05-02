package mchorse.mappet.network.client.factions;

import mchorse.mappet.client.gui.GuiJournalScreen;
import mchorse.mappet.network.common.factions.PacketFactions;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerFactions extends ClientMessageHandler<PacketFactions>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketFactions message)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (mc.currentScreen instanceof GuiJournalScreen)
        {
            ((GuiJournalScreen) mc.currentScreen).fillFactions(message.factions, message.states);
        }
    }
}