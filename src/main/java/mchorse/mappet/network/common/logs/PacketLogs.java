package mchorse.mappet.network.common.logs;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketLogs implements IMessage
{
    public String text;

    public PacketLogs()
    {
    }

    public PacketLogs(String line)
    {
        this.text = line;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.text);
    }
}