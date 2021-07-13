package mchorse.mappet.network.common.scripts;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketRepl implements IMessage
{
    public String code;

    public PacketRepl()
    {}

    public PacketRepl(String code)
    {
        this.code = code;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.code = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.code);
    }
}