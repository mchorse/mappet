package mchorse.mappet.network.common.blocks;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.blocks.BlockTrigger;
import mchorse.mappet.tile.TileTrigger;
import mchorse.mclib.utils.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketEditTrigger implements IMessage
{
    public BlockPos pos;
    public NBTTagCompound left = new NBTTagCompound();
    public NBTTagCompound right = new NBTTagCompound();
    public boolean collidable;

    public PacketEditTrigger()
    {}

    public PacketEditTrigger(TileTrigger tile)
    {
        this(tile.getPos(), tile.leftClick.serializeNBT(), tile.rightClick.serializeNBT(), tile.getWorld().getBlockState(tile.getPos()).getValue(BlockTrigger.COLLIDABLE));
    }

    public PacketEditTrigger(BlockPos pos, NBTTagCompound left, NBTTagCompound right, boolean collidable)
    {
        this.pos = pos;
        this.left = left;
        this.right = right;
        this.collidable = collidable;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.left = NBTUtils.readInfiniteTag(buf);
        this.right = NBTUtils.readInfiniteTag(buf);
        this.collidable = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        ByteBufUtils.writeTag(buf, this.left);
        ByteBufUtils.writeTag(buf, this.right);
        buf.writeBoolean(this.collidable);
    }
}