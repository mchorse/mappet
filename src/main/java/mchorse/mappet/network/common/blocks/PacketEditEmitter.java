package mchorse.mappet.network.common.blocks;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.tile.TileEmitter;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketEditEmitter implements IMessage
{
    public BlockPos pos;
    public String expression = "";
    public float radius;

    public PacketEditEmitter()
    {}

    public PacketEditEmitter(TileEmitter tile)
    {
        this(tile.getPos(), tile.getExpression(), tile.getRadius());
    }

    public PacketEditEmitter(BlockPos pos, String expression, float radius)
    {
        this.pos = pos;
        this.expression = expression;
        this.radius = radius;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.expression = ByteBufUtils.readUTF8String(buf);
        this.radius = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        ByteBufUtils.writeUTF8String(buf, this.expression);
        buf.writeFloat(this.radius);
    }
}