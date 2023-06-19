package mchorse.mappet.network.common.utils;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketChangedBoundingBox implements IMessage
{
    public BlockPos pos;
    public Vec3d boundingBoxPos1;
    public Vec3d boundingBoxPos2;

    public PacketChangedBoundingBox()
    {

    }

    public PacketChangedBoundingBox(BlockPos pos, Vec3d boundingBoxPos1, Vec3d boundingBoxPos2)
    {
        this.pos = pos;
        this.boundingBoxPos1 = boundingBoxPos1;
        this.boundingBoxPos2 = boundingBoxPos2;
    }
    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.boundingBoxPos1 = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
        this.boundingBoxPos2 = new Vec3d(buf.readDouble(), buf.readDouble(), buf.readDouble());
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        buf.writeDouble(boundingBoxPos1.x);
        buf.writeDouble(boundingBoxPos1.y);
        buf.writeDouble(boundingBoxPos1.z);
        buf.writeDouble(boundingBoxPos2.x);
        buf.writeDouble(boundingBoxPos2.y);
        buf.writeDouble(boundingBoxPos2.z);
    }
}
