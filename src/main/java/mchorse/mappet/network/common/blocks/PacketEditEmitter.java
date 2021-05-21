package mchorse.mappet.network.common.blocks;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.tile.TileEmitter;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketEditEmitter implements IMessage
{
    public BlockPos pos;
    public NBTTagCompound checker;
    public float radius;

    public PacketEditEmitter()
    {}

    public PacketEditEmitter(TileEmitter tile)
    {
        this(tile.getPos(), tile.getChecker().toNBT(), tile.getRadius());
    }

    public PacketEditEmitter(BlockPos pos, NBTTagCompound checker, float radius)
    {
        this.pos = pos;
        this.checker = checker;
        this.radius = radius;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.checker = ByteBufUtils.readTag(buf);
        this.radius = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        ByteBufUtils.writeTag(buf, this.checker);
        buf.writeFloat(this.radius);
    }
}