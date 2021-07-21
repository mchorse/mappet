package mchorse.mappet.network.common.blocks;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.conditions.Checker;
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
    public int update;
    public boolean disable;

    public PacketEditEmitter()
    {}

    public PacketEditEmitter(TileEmitter tile)
    {
        this(tile.getPos(), tile.getChecker().toNBT(), tile.getRadius(), tile.getUpdate(), tile.getDisable());
    }

    public PacketEditEmitter(BlockPos pos, NBTTagCompound checker, float radius, int update, boolean disable)
    {
        this.pos = pos;
        this.checker = checker;
        this.radius = radius;
        this.update = update;
        this.disable = disable;
    }

    public Checker createChecker()
    {
        Checker checker = new Checker();

        checker.deserializeNBT(this.checker);

        return checker;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.checker = ByteBufUtils.readTag(buf);
        this.radius = buf.readFloat();
        this.update = buf.readInt();
        this.disable = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        ByteBufUtils.writeTag(buf, this.checker);
        buf.writeFloat(this.radius);
        buf.writeInt(this.update);
        buf.writeBoolean(this.disable);
    }
}