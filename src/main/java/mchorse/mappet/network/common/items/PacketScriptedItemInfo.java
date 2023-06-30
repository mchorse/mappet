package mchorse.mappet.network.common.items;

import io.netty.buffer.ByteBuf;
import mchorse.mclib.utils.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketScriptedItemInfo implements IMessage
{
    public NBTTagCompound tag;
    public NBTTagCompound stackTag;
    public int entity;

    public PacketScriptedItemInfo()
    {
        this.tag = new NBTTagCompound();
    }

    public PacketScriptedItemInfo(NBTTagCompound tag, NBTTagCompound stackTag, int entity)
    {
        this.tag = tag;
        this.stackTag = stackTag;
        this.entity = entity;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.tag = NBTUtils.readInfiniteTag(buf);

        if (buf.readBoolean())
        {
            this.stackTag = NBTUtils.readInfiniteTag(buf);
        }

        this.entity = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, this.tag);

        buf.writeBoolean(this.stackTag != null);

        if (this.stackTag != null)
        {
            ByteBufUtils.writeTag(buf, this.stackTag);
        }

        buf.writeInt(this.entity);
    }
}
