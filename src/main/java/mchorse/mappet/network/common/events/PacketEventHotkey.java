package mchorse.mappet.network.common.events;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketEventHotkey implements IMessage
{
    public int keycode;

    public PacketEventHotkey()
    {}

    public PacketEventHotkey(int keycode)
    {
        this.keycode = keycode;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.keycode = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.keycode);
    }
}