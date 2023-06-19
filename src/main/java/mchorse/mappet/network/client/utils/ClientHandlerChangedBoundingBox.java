package mchorse.mappet.network.client.utils;

import mchorse.mappet.network.common.utils.PacketChangedBoundingBox;
import mchorse.mappet.tile.TileTrigger;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.tileentity.TileEntity;

public class ClientHandlerChangedBoundingBox extends ClientMessageHandler<PacketChangedBoundingBox>
{

    @Override
    public void run(EntityPlayerSP entityPlayerSP, PacketChangedBoundingBox message)
    {
        TileEntity tile = Minecraft.getMinecraft().world.getTileEntity(message.pos);

        if (tile instanceof TileTrigger)
        {
            ((TileTrigger)tile).boundingBoxPos1 = message.boundingBoxPos1;
            ((TileTrigger)tile).boundingBoxPos2 = message.boundingBoxPos2;
        }

    }
}
