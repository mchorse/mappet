package mchorse.mappet.network.common.quests;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.quests.chains.QuestStatus;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketQuestAction implements IMessage
{
    public String id;
    public QuestStatus status;

    public PacketQuestAction()
    {}

    public PacketQuestAction(String id, QuestStatus status)
    {
        this.id = id;
        this.status = status;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.id = ByteBufUtils.readUTF8String(buf);
        this.status = QuestStatus.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.id);
        buf.writeInt(this.status.ordinal());
    }
}