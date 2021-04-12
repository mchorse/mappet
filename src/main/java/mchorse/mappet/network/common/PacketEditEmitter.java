package mchorse.mappet.network.common;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.tile.TileEmitter;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketEditEmitter implements IMessage
{
    public BlockPos pos;
    public String expression = "";

    public PacketEditEmitter()
    {}

    public PacketEditEmitter(TileEmitter tile)
    {
        this(tile.getPos(), tile.expression);
    }

    public PacketEditEmitter(BlockPos pos, String expression)
    {
        this.pos = pos;
        this.expression = expression;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.expression = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        ByteBufUtils.writeUTF8String(buf, this.expression);
    }
}