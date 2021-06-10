package mchorse.mappet.network.common.scripts;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketEntityRotations implements IMessage
{
    public int entityId;
    public float yaw;
    public float yawHead;
    public float pitch;

    public PacketEntityRotations()
    {}

    public PacketEntityRotations(int entityId, float yaw, float yawHead, float pitch)
    {
        this.entityId = entityId;
        this.yaw = yaw;
        this.yawHead = yawHead;
        this.pitch = pitch;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityId = buf.readInt();
        this.yaw = buf.readFloat();
        this.yawHead = buf.readFloat();
        this.pitch = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.entityId);
        buf.writeFloat(this.yaw);
        buf.writeFloat(this.yawHead);
        buf.writeFloat(this.pitch);
    }
}