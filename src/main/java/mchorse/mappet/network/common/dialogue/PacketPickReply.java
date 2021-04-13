package mchorse.mappet.network.common.dialogue;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketPickReply implements IMessage
{
    public int index;

    public PacketPickReply()
    {}

    public PacketPickReply(int index)
    {
        this.index = index;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.index = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.index);
    }
}