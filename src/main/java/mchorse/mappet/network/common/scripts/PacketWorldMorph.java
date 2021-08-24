package mchorse.mappet.network.common.scripts;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.client.morphs.WorldMorph;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketWorldMorph implements IMessage
{
    public WorldMorph morph;

    public PacketWorldMorph()
    {
        this.morph = new WorldMorph();
    }

    public PacketWorldMorph(WorldMorph morph)
    {
        this.morph = morph;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.morph.fromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        this.morph.toBytes(buf);
    }
}
