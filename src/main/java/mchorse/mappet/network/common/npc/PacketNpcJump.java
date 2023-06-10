package mchorse.mappet.network.common.npc;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketNpcJump implements IMessage
{
    public int entityId;
    public float jumpPower;

    public PacketNpcJump()
    {
    }

    public PacketNpcJump(int entityId, float jumpPower)
    {
        this.entityId = entityId;
        this.jumpPower = jumpPower;
    }

    public float getJumpPower()
    {
        return jumpPower;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.entityId);
        buf.writeFloat(jumpPower);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.entityId = buf.readInt();
        this.jumpPower = buf.readFloat();
    }
}