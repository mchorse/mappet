package mchorse.mappet.network.common.scripts;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketPlayerSkin implements IMessage
{
    public String skin = "";

    public PacketPlayerSkin()
    {}

    public PacketPlayerSkin(String skin)
    {
        this.skin = skin;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.skin = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.skin);
    }
}