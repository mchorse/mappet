package mchorse.mappet.network.common.content;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.utils.ContentType;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class PacketContentBase implements IMessage
{
    public ContentType type;
    public int requestId = -1;

    public PacketContentBase()
    {}

    public PacketContentBase(ContentType type)
    {
        this.type = type;
    }

    public PacketContentBase(ContentType type, int requestId)
    {
        this.type = type;
        this.requestId = requestId;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.type = ContentType.values()[buf.readInt()];
        this.requestId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.type.ordinal());
        buf.writeInt(this.requestId);
    }
}