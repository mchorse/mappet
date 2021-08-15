package mchorse.mappet.network.common.events;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketEventHotkey implements IMessage
{
    public int keycode;
    public boolean down;

    public PacketEventHotkey()
    {}

    public PacketEventHotkey(int keycode, boolean down)
    {
        this.keycode = keycode;
        this.down = down;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.keycode = buf.readInt();
        this.down = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.keycode);
        buf.writeBoolean(this.down);
    }
}