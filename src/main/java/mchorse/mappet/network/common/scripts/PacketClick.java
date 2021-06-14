package mchorse.mappet.network.common.scripts;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketClick implements IMessage
{
    public EnumHand hand = EnumHand.MAIN_HAND;

    public PacketClick()
    {}

    public PacketClick(EnumHand hand)
    {
        this.hand = hand;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.hand = EnumHand.values()[buf.readInt()];
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.hand.ordinal());
    }
}
