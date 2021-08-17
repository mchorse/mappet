package mchorse.mappet.network.common.events;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.misc.ServerSettings;
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
    public boolean journalTrigger;

    public PacketEventHotkeys()
    {}

    public PacketEventHotkeys(ServerSettings settings)
    {
        this.hotkeys.addAll(settings.hotkeys.hotkeys);
        this.journalTrigger = !settings.playerJournal.isEmpty();
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        for (int i = 0, c = buf.readInt(); i < c; i++)
        {
            this.hotkeys.add(new TriggerHotkey(buf.readInt(), buf.readBoolean()));
        }

        this.journalTrigger = buf.readBoolean();
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

        buf.writeBoolean(this.journalTrigger);
    }
}