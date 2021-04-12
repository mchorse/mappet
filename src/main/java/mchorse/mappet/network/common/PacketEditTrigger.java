package mchorse.mappet.network.common;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.tile.TileEmitter;
import mchorse.mappet.tile.TileTrigger;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class PacketEditTrigger implements IMessage
{
    public BlockPos pos;
    public String left = "";
    public String right = "";

    public PacketEditTrigger()
    {}

    public PacketEditTrigger(TileTrigger tile)
    {
        this(tile.getPos(), tile.leftClick, tile.rightClick);
    }

    public PacketEditTrigger(BlockPos pos, String left, String right)
    {
        this.pos = pos;
        this.left = left;
        this.right = right;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        this.left = ByteBufUtils.readUTF8String(buf);
        this.right = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(this.pos.getX());
        buf.writeInt(this.pos.getY());
        buf.writeInt(this.pos.getZ());
        ByteBufUtils.writeUTF8String(buf, this.left);
        ByteBufUtils.writeUTF8String(buf, this.right);
    }
}