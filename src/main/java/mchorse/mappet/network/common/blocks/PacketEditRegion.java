package mchorse.mappet.network.common.blocks;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.tile.TileRegion;
import mchorse.mclib.utils.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketEditRegion implements IMessage
{
    public boolean open;
    public BlockPos pos;
    public NBTTagCompound tag;

    public PacketEditRegion()
    {}

    public PacketEditRegion(TileRegion tile)
    {
        this(tile.getPos(), tile.region.serializeNBT());
    }

    public PacketEditRegion(BlockPos pos, NBTTagCompound tag)
    {
        this.pos = pos;
        this.tag = tag;
    }

    public PacketEditRegion open()
    {
        this.open = true;

        return this;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.open = buf.readBoolean();
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.tag = NBTUtils.readInfiniteTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeBoolean(this.open);
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        ByteBufUtils.writeTag(buf, this.tag);
    }
}