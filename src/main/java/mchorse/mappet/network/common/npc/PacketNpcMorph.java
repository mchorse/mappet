package mchorse.mappet.network.common.npc;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.entities.EntityNpc;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketNpcMorph implements IMessage
{
    public int id;
    public AbstractMorph morph;

    public PacketNpcMorph()
    {}

    public PacketNpcMorph(EntityNpc npc)
    {
        this(npc.getEntityId(), npc.getMorph());
    }

    public PacketNpcMorph(int id, AbstractMorph morph)
    {
        this.id = id;
        this.morph = morph;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.id = buf.readInt();
        this.morph = MorphUtils.morphFromBuf(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.id);
        MorphUtils.morphToBuf(buf, this.morph);
    }
}