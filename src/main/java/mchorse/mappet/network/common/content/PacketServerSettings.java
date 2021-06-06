package mchorse.mappet.network.common.content;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketServerSettings implements IMessage
{
    public NBTTagCompound tag;

    public PacketServerSettings()
    {}

    public PacketServerSettings(NBTTagCompound tag)
    {
        this.tag = tag;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.tag = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeTag(buf, this.tag);
    }
}