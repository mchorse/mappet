package mchorse.mappet.network.common.npc;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.entities.EntityNpc;
import mchorse.mappet.utils.NpcStateUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

//This used to be `PacketNpcMorph`, but I (Otaku) changed it to `PacketNpcStateChange` because it's more accurate.
public class PacketNpcStateChange implements IMessage
{
    public int id;
    public NpcState state;

    public PacketNpcStateChange()
    {}

    public PacketNpcStateChange(EntityNpc npc)
    {
        this(npc.getEntityId(), npc.getState());
    }

    public PacketNpcStateChange(int id, NpcState state)
    {
        this.id = id;
        this.state = state;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.id = buf.readInt();
        this.state = NpcStateUtils.stateFromBuf(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.id);
        NpcStateUtils.stateToBuf(buf, this.state);
    }
}