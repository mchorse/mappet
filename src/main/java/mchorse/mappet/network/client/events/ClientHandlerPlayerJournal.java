package mchorse.mappet.network.client.events;

import mchorse.mappet.client.gui.GuiJournalScreen;
import mchorse.mappet.network.common.events.PacketPlayerJournal;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerPlayerJournal extends ClientMessageHandler<PacketPlayerJournal>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketPlayerJournal message)
    {
        Minecraft mc = Minecraft.getMinecraft();

        mc.displayGuiScreen(new GuiJournalScreen(mc));
    }
}