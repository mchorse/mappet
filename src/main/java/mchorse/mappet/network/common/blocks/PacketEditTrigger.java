package mchorse.mappet.network.common.blocks;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.tile.TileEmitter;
import mchorse.mappet.tile.TileTrigger;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketEditTrigger implements IMessage
{
    public BlockPos pos;
    public NBTTagCompound left = new NBTTagCompound();
    public NBTTagCompound right = new NBTTagCompound();

    public PacketEditTrigger()
    {}

    public PacketEditTrigger(TileTrigger tile)
    {
        this(tile.getPos(), tile.leftClick.serializeNBT(), tile.rightClick.serializeNBT());
    }

    public PacketEditTrigger(BlockPos pos, NBTTagCompound left, NBTTagCompound right)
    {
        this.pos = pos;
        this.left = left;
        this.right = right;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.left = ByteBufUtils.readTag(buf);
        this.right = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        ByteBufUtils.writeTag(buf, this.left);
        ByteBufUtils.writeTag(buf, this.right);
    }
}