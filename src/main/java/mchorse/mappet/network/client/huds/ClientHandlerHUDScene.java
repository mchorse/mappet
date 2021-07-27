package mchorse.mappet.network.client.huds;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.huds.HUDScene;
import mchorse.mappet.client.RenderingHandler;
import mchorse.mappet.network.common.huds.PacketHUDScene;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientHandlerHUDScene extends ClientMessageHandler<PacketHUDScene>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketHUDScene message)
    {
        if (message.tag == null)
        {
            if (message.id.isEmpty())
            {
                RenderingHandler.stage.scenes.clear();
            }
            else
            {
                RenderingHandler.stage.scenes.remove(message.id);
            }
        }
        else
        {
            HUDScene scene = Mappet.huds.create(message.id, message.tag);

            RenderingHandler.stage.scenes.put(message.id, scene);
        }
    }
}