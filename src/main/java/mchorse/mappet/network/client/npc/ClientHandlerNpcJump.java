package mchorse.mappet.network.client.npc;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class ClientHandlerNpcJump implements IMessage {
    public int entityId;
    public float jumpPower;

    public ClientHandlerNpcJump() {
    }

    public ClientHandlerNpcJump(int entityId, float jumpPower) {
        this.entityId = entityId;
        this.jumpPower = jumpPower;
    }

    public float getJumpPower() {
        return jumpPower;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeFloat(jumpPower);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.entityId = buf.readInt();
        this.jumpPower = buf.readFloat();
    }
}