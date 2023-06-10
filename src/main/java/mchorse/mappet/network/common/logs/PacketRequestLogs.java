package mchorse.mappet.network.common.logs;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketRequestLogs implements IMessage
{

    public String lastLogTime = "";

    public PacketRequestLogs()
    {
    }

    public PacketRequestLogs setLastDate(String lastLogTime)
    {
        this.lastLogTime = lastLogTime;

        return this;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.lastLogTime = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.lastLogTime);
    }
}