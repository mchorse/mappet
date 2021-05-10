package mchorse.mappet.network.common.npc;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketNpcTool implements IMessage
{
    public String npc = "";
    public String state = "";

    public PacketNpcTool()
    {}

    public PacketNpcTool(String npc, String state)
    {
        this.npc = npc;
        this.state = state;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.npc = ByteBufUtils.readUTF8String(buf);
        this.state = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.npc);
        ByteBufUtils.writeUTF8String(buf, this.state);
    }
}