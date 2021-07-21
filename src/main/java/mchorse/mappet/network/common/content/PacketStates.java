package mchorse.mappet.network.common.content;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketStates implements IMessage
{
    public String target;
    public NBTTagCompound states;

    public PacketStates()
    {}

    public PacketStates(String target, NBTTagCompound states)
    {
        this.target = target;
        this.states = states;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.target = ByteBufUtils.readUTF8String(buf);
        this.states = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.target);
        ByteBufUtils.writeTag(buf, this.states);
    }
}