package mchorse.mappet.network.client.scripts;

import mchorse.mappet.client.RenderingHandler;
import mchorse.mappet.network.common.scripts.PacketWorldMorph;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerWorldMorph extends ClientMessageHandler<PacketWorldMorph>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketWorldMorph message)
    {
        if (message.morph.morph != null)
        {
            RenderingHandler.worldMorphs.add(message.morph);
        }
    }
}