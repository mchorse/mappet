package mchorse.mappet.network.common.factions;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.factions.Faction;
import mchorse.mclib.utils.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.HashMap;
import java.util.Map;

public class PacketFactions implements IMessage
{
    public Map<String, Faction> factions = new HashMap<String, Faction>();
    public Map<String, Double> states = new HashMap<String, Double>();

    public PacketFactions()
    {}

    public PacketFactions(Map<String, Faction> factions, Map<String, Double> states)
    {
        this.factions.putAll(factions);
        this.states.putAll(states);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        for (int i = 0, c = buf.readInt(); i < c; i++)
        {
            String key = ByteBufUtils.readUTF8String(buf);
            NBTTagCompound tag = NBTUtils.readInfiniteTag(buf);
            Faction faction = Mappet.factions.create(key, tag);

            if (faction != null)
            {
                this.factions.put(key, faction);
            }
        }

        for (int i = 0, c = buf.readInt(); i < c; i++)
        {
            String key = ByteBufUtils.readUTF8String(buf);
            double value = buf.readDouble();

            this.states.put(key, value);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.factions.size());

        for (Map.Entry<String, Faction> entry : this.factions.entrySet())
        {
            ByteBufUtils.writeUTF8String(buf, entry.getKey());
            ByteBufUtils.writeTag(buf, entry.getValue().serializeNBT());
        }

        buf.writeInt(this.states.size());

        for (Map.Entry<String, Double> entry : this.states.entrySet())
        {
            ByteBufUtils.writeUTF8String(buf, entry.getKey());
            buf.writeDouble(entry.getValue());
        }
    }
}