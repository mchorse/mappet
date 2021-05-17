package mchorse.mappet.network.common.events;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.events.hotkeys.EventHotkey;
import mchorse.mappet.api.events.hotkeys.EventHotkeys;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.List;

public class PacketEventPlayerHotkeys implements IMessage
{
    public List<Integer> hotkeys = new ArrayList<Integer>();

    public PacketEventPlayerHotkeys()
    {}

    public PacketEventPlayerHotkeys(EventHotkeys hotkeys)
    {
        for (EventHotkey hotkey : hotkeys.hotkeys)
        {
            this.hotkeys.add(hotkey.keycode);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        for (int i = 0, c = buf.readInt(); i < c; i++)
        {
            this.hotkeys.add(buf.readInt());
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.hotkeys.size());

        for (Integer integer : this.hotkeys)
        {
            buf.writeInt(integer);
        }
    }
}