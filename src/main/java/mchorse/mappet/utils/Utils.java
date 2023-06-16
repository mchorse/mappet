package mchorse.mappet.utils;

import mchorse.blockbuster.common.tileentity.TileEntityModel;
import mchorse.blockbuster.network.common.PacketModifyModelBlock;
import mchorse.mappet.network.common.blocks.PacketEditConditionModel;
import mchorse.mappet.tile.TileConditionModel;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class Utils
{
    public static Charset getCharset()
    {
        return StandardCharsets.UTF_8;
    }

    /**
     * This method sends the packet informing about the model block update
     */
    public static <T> void sendModelUpdatePacket(T modelBlock)
    {
        try
        {
            if (modelBlock instanceof TileEntityModel)
            {
                TileEntityModel bbModelBlock = (TileEntityModel) modelBlock;
                PacketModifyModelBlock message = new PacketModifyModelBlock(bbModelBlock.getPos(), bbModelBlock, true);
                mchorse.blockbuster.network.Dispatcher.DISPATCHER.get().sendToAll(message);
            }
            else if (modelBlock instanceof TileConditionModel)
            {
                TileConditionModel conditionModelBlock = (TileConditionModel) modelBlock;
                NBTTagCompound tag = new NBTTagCompound();
                PacketEditConditionModel message = new PacketEditConditionModel(conditionModelBlock.getPos(), conditionModelBlock.toNBT(tag));
                mchorse.blockbuster.network.Dispatcher.DISPATCHER.get().sendToAll(message);
            }
            else
            {
                throw new IllegalArgumentException("Invalid modelBlock type");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}