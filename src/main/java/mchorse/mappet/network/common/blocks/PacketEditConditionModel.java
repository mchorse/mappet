package mchorse.mappet.network.common.blocks;

import io.netty.buffer.ByteBuf;
import mchorse.mclib.utils.NBTUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;


public class PacketEditConditionModel implements IMessage
{

    public BlockPos pos;
    public boolean isEdit;
    public NBTTagCompound tag;

    public PacketEditConditionModel()
    {
    }

    public PacketEditConditionModel(BlockPos pos, NBTTagCompound tag)
    {
        this.isEdit = true;
        this.pos = pos;
        this.tag = tag;
    }

    public PacketEditConditionModel setIsEdit(boolean isEdit)
    {
        this.isEdit = isEdit;
        return this;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.isEdit = buf.readBoolean();
        this.tag = NBTUtils.readInfiniteTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        buf.writeBoolean(this.isEdit);
        ByteBufUtils.writeTag(buf, this.tag);
    }
}