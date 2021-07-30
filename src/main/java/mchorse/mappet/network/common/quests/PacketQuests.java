package mchorse.mappet.network.common.quests;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.quests.Quest;
import mchorse.mappet.api.quests.Quests;
import mchorse.mclib.utils.NBTUtils;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.LinkedHashMap;
import java.util.Map;

public class PacketQuests implements IMessage
{
    public Map<String, Quest> quests = new LinkedHashMap<String, Quest>();

    public PacketQuests()
    {}

    public PacketQuests(Quests quests)
    {
        this.quests.putAll(quests.quests);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        for (int i = 0, c = buf.readInt(); i < c; i++)
        {
            String id = ByteBufUtils.readUTF8String(buf);

            this.quests.put(id, Mappet.quests.create(id, NBTUtils.readInfiniteTag(buf)));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.quests.size());

        for (Map.Entry<String, Quest> entry : this.quests.entrySet())
        {
            ByteBufUtils.writeUTF8String(buf, entry.getKey());
            ByteBufUtils.writeTag(buf, entry.getValue().serializeNBT());
        }
    }
}