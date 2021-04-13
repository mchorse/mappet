package mchorse.mappet.network.common.content;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.utils.ContentType;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;

public class PacketContentData extends PacketContentBase
{
    public String name = "";
    public String rename;
    public NBTTagCompound data;

    public PacketContentData()
    {
        super();
    }

    public PacketContentData(ContentType type, String name)
    {
        super(type);

        this.name = name;
    }

    public PacketContentData(ContentType type, String name, NBTTagCompound data)
    {
        this(type, name);

        this.data = data;
    }

    public PacketContentData rename(String rename)
    {
        this.rename = rename;

        return this;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        super.fromBytes(buf);

        this.name = ByteBufUtils.readUTF8String(buf);

        if (buf.readBoolean())
        {
            this.data = ByteBufUtils.readTag(buf);
        }

        if (buf.readBoolean())
        {
            this.rename = ByteBufUtils.readUTF8String(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        super.toBytes(buf);

        ByteBufUtils.writeUTF8String(buf, this.name);

        buf.writeBoolean(this.data != null);

        if (this.data != null)
        {
            ByteBufUtils.writeTag(buf, this.data);
        }

        buf.writeBoolean(this.rename != null);

        if (this.rename != null)
        {
            ByteBufUtils.writeUTF8String(buf, this.rename);
        }
    }
}