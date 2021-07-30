package mchorse.mappet.network.common.quests;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.quests.Quest;
import mchorse.mclib.utils.NBTUtils;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketQuest implements IMessage
{
    public String id;
    public Quest quest;

    public PacketQuest()
    {}

    public PacketQuest(String id, Quest quest)
    {
        this.id = id;
        this.quest = quest;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.id = ByteBufUtils.readUTF8String(buf);

        if (buf.readBoolean())
        {
            this.quest = Mappet.quests.create(this.id, NBTUtils.readInfiniteTag(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.id);

        buf.writeBoolean(this.quest != null);

        if (this.quest != null)
        {
            ByteBufUtils.writeTag(buf, this.quest.serializeNBT());
        }
    }
}