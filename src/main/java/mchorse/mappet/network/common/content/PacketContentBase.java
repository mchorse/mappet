package mchorse.mappet.network.common.content;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.utils.ContentType;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class PacketContentBase implements IMessage
{
    public ContentType type;

    public PacketContentBase()
    {}

    public PacketContentBase(ContentType type)
    {
        this.type = type;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.type = ContentType.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.type.ordinal());
    }
}