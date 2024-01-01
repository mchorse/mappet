package mchorse.mappet.network.common.content;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.utils.IContentType;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketContentRequestData extends PacketContentBase
{
    public String name = "";

    public PacketContentRequestData()
    {
        super();
    }

    public PacketContentRequestData(IContentType type, String name)
    {
        super(type);

        this.name = name;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);

        this.name = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);

        ByteBufUtils.writeUTF8String(buf, this.name);
    }
}