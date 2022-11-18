package mchorse.mappet.network.common.content;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.utils.ContentType;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketContentFolder extends PacketContentBase
{
    public String name = "";
    public String path = "";
    public String rename;
    public Boolean delete = false;

    public PacketContentFolder()
    {
        super();
    }
    public PacketContentFolder(ContentType type, String name, String path)
    {
        super(type);
        this.path = path;
        this.name = name;
    }

    public PacketContentFolder rename(String rename)
    {
        this.rename = rename;

        return this;
    }

    public PacketContentFolder delete()
    {
        this.delete = true;

        return this;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);

        this.name = ByteBufUtils.readUTF8String(buf);

        this.path = ByteBufUtils.readUTF8String(buf);

        if (buf.readBoolean())
        {
            this.rename = ByteBufUtils.readUTF8String(buf);
        }

        this.delete = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);

        ByteBufUtils.writeUTF8String(buf, this.name);

        ByteBufUtils.writeUTF8String(buf, this.path);

        buf.writeBoolean(this.rename != null);

        if (this.rename != null)
        {
            ByteBufUtils.writeUTF8String(buf, this.rename);
        }

        buf.writeBoolean(this.delete);
    }
}
