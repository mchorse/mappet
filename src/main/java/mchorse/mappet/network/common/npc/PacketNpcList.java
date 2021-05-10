package mchorse.mappet.network.common.npc;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class PacketNpcList implements IMessage
{
    public List<String> npcs = new ArrayList<String>();
    public List<String> states = new ArrayList<String>();
    public boolean isStates;

    public PacketNpcList()
    {}

    public PacketNpcList(Collection<String> npcs, Collection<String> states)
    {
        this.npcs.addAll(npcs);
        this.states.addAll(states);
    }

    public PacketNpcList states()
    {
        this.isStates = true;

        return this;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        for (int i = 0, c = buf.readInt(); i < c; i++)
        {
            this.npcs.add(ByteBufUtils.readUTF8String(buf));
        }

        for (int i = 0, c = buf.readInt(); i < c; i++)
        {
            this.states.add(ByteBufUtils.readUTF8String(buf));
        }

        this.isStates = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.npcs.size());

        for (String string : this.npcs)
        {
            ByteBufUtils.writeUTF8String(buf, string);
        }

        buf.writeInt(this.states.size());

        for (String string : this.states)
        {
            ByteBufUtils.writeUTF8String(buf, string);
        }

        buf.writeBoolean(this.isStates);
    }
}