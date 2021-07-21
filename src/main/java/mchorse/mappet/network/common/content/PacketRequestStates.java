package mchorse.mappet.network.common.content;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketRequestStates implements IMessage
{
    public String target;

    public PacketRequestStates()
    {}

    public PacketRequestStates(String target)
    {
        this.target = target;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.target = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.target);
    }
}