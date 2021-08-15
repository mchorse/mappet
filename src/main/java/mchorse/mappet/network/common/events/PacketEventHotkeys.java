package mchorse.mappet.network.common.events;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.misc.hotkeys.TriggerHotkey;
import mchorse.mappet.api.misc.hotkeys.TriggerHotkeys;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * This packet reads only the key code and toggle mode (so no trigger or condition)!
 */
public class PacketEventHotkeys implements IMessage
{
    public List<TriggerHotkey> hotkeys = new ArrayList<TriggerHotkey>();

    public PacketEventHotkeys()
    {}

    public PacketEventHotkeys(TriggerHotkeys hotkeys)
    {
        this.hotkeys.addAll(hotkeys.hotkeys);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        for (int i = 0, c = buf.readInt(); i < c; i++)
        {
            this.hotkeys.add(new TriggerHotkey(buf.readInt(), buf.readBoolean()));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.hotkeys.size());

        for (TriggerHotkey hotkey : this.hotkeys)
        {
            buf.writeInt(hotkey.keycode);
            buf.writeBoolean(hotkey.toggle);
        }
    }
}