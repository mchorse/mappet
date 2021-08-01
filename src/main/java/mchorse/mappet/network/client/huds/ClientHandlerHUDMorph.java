package mchorse.mappet.network.client.huds;

import mchorse.mappet.api.huds.HUDMorph;
import mchorse.mappet.api.huds.HUDScene;
import mchorse.mappet.client.RenderingHandler;
import mchorse.mappet.network.common.huds.PacketHUDMorph;
import mchorse.mclib.network.ClientMessageHandler;
import mchorse.metamorph.api.MorphManager;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerHUDMorph extends ClientMessageHandler<PacketHUDMorph>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketHUDMorph message)
    {
        HUDScene scene = RenderingHandler.stage.scenes.get(message.id);

        if (scene != null && message.index >= 0 && message.index < scene.morphs.size())
        {
            HUDMorph morph = scene.morphs.get(message.index);

            morph.morph.set(MorphManager.INSTANCE.morphFromNBT(message.morph));
        }
    }
}