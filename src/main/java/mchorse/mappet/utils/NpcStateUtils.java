package mchorse.mappet.utils;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mclib.utils.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class NpcStateUtils
{

    public NpcStateUtils()
    {
    }

    public static void stateToBuf(ByteBuf buffer, NpcState state)
    {
        NBTTagCompound tag = state.serializeNBT();

        ByteBufUtils.writeTag(buffer, tag);
    }

    public static NpcState stateFromBuf(ByteBuf buffer)
    {
        NBTTagCompound tag = NBTUtils.readInfiniteTag(buffer);

        NpcState state = new NpcState();
        state.deserializeNBT(tag);

        return state;
    }
}
