package mchorse.mappet.network.client.npc;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.client.gui.GuiNpcStateScreen;
import mchorse.mappet.network.common.npc.PacketNpcState;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerNpcState extends ClientMessageHandler<PacketNpcState>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketNpcState message)
    {
        Minecraft mc = Minecraft.getMinecraft();
        NpcState state = new NpcState();

        state.deserializeNBT(message.state);
        mc.displayGuiScreen(new GuiNpcStateScreen(mc, message.entityId, state));
    }
}