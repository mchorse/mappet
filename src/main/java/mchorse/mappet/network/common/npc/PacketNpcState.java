package mchorse.mappet.network.common.npc;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketNpcState implements IMessage
{
    public int entityId;
    public NBTTagCompound state;

    public PacketNpcState()
    {}

    public PacketNpcState(int entityId, NBTTagCompound state)
    {
        this.entityId = entityId;
        this.state = state;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityId = buf.readInt();
        this.state = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.entityId);
        ByteBufUtils.writeTag(buf, this.state);
    }
}