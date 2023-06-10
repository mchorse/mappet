package mchorse.mappet.network.common.items;

import io.netty.buffer.ByteBuf;
import mchorse.mclib.utils.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketScriptedItemInfo implements IMessage
{
    public NBTTagCompound tag;
    public int entity;

    public PacketScriptedItemInfo()
    {
        this.tag = new NBTTagCompound();
    }

    public PacketScriptedItemInfo(NBTTagCompound tag, int entity)
    {
        this.tag = tag;
        this.entity = entity;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.tag = NBTUtils.readInfiniteTag(buf);
        this.entity = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, this.tag);
        buf.writeInt(this.entity);
    }
}
