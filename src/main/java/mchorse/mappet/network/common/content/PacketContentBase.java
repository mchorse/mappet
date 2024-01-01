package mchorse.mappet.network.common.content;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.utils.IContentType;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class PacketContentBase implements IMessage
{
    public IContentType type;
    public int requestId = -1;

    public PacketContentBase()
    {}

    public PacketContentBase(IContentType type)
    {
        this.type = type;
    }

    public PacketContentBase(IContentType type, int requestId)
    {
        this.type = type;
        this.requestId = requestId;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.type = IContentType.getType(ByteBufUtils.readUTF8String(buf));
        this.requestId = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.type.getName());
        buf.writeInt(this.requestId);
    }
}