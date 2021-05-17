package mchorse.mappet.network.common.events;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketEventRequestHotkeys implements IMessage
{
    public PacketEventRequestHotkeys()
    {}

    @Override
    public void fromBytes(ByteBuf buf)
    {}

    @Override
    public void toBytes(ByteBuf buf)
    {}
}