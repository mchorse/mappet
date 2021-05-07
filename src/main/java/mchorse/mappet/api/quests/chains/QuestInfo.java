package mchorse.mappet.api.quests.chains;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.quests.Quest;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class QuestInfo implements IMessage
{
    public Quest quest;
    public QuestStatus status;

    public QuestInfo()
    {}

    public QuestInfo(Quest quest, QuestStatus status)
    {
        this.quest = quest;
        this.status = status;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.status = QuestStatus.values()[buf.readInt()];

        String id = ByteBufUtils.readUTF8String(buf);
        NBTTagCompound tag = ByteBufUtils.readTag(buf);

        this.quest = Mappet.quests.create(id, tag);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.status.ordinal());

        ByteBufUtils.writeUTF8String(buf, this.quest.getId());
        ByteBufUtils.writeTag(buf, this.quest.serializeNBT());
    }
}