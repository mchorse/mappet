package mchorse.mappet.network.client.npc;

import mchorse.mappet.client.gui.GuiNpcToolScreen;
import mchorse.mappet.network.common.npc.PacketNpcList;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerNpcList extends ClientMessageHandler<PacketNpcList>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketNpcList message)
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (message.isStates)
        {
            if (mc.currentScreen instanceof GuiNpcToolScreen)
            {
                GuiNpcToolScreen tool = (GuiNpcToolScreen) mc.currentScreen;

                tool.states.clear();
                tool.states.add(message.states);
                tool.states.sort();
            }
        }
        else
        {
            mc.displayGuiScreen(new GuiNpcToolScreen(mc, message.npcs, message.states));
        }
    }
}