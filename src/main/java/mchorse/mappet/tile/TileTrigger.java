package mchorse.mappet.tile;

import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.blocks.BlockTrigger;
import mchorse.mappet.network.Dispatcher;
import mchorse.mappet.network.common.utils.PacketChangedBoundingBox;
import mchorse.mappet.utils.NBTUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TileTrigger extends TileEntity
{
    public Trigger leftClick = new Trigger();
    public Trigger rightClick = new Trigger();
    public Vec3d boundingBoxPos1 = new Vec3d(0, 0, 0);
    public Vec3d boundingBoxPos2 = new Vec3d(1, 1, 1);

    public void set(NBTTagCompound left, NBTTagCompound right, boolean collidable, Vec3d boundingBoxPos1, Vec3d boundingBoxPos2)
    {
        this.leftClick = new Trigger();
        this.leftClick.deserializeNBT(left);
        this.rightClick = new Trigger();
        this.rightClick.deserializeNBT(right);
        this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).withProperty(BlockTrigger.COLLIDABLE, collidable));

        this.boundingBoxPos1 = boundingBoxPos1;
        this.boundingBoxPos2 = boundingBoxPos2;

        this.sendChangesToAll();

        this.markDirty();
    }

    public void sendChangesToAll()
    {
        if (this.world.isRemote)
        {
            return;
        }

        PacketChangedBoundingBox message = new PacketChangedBoundingBox(this.pos, this.boundingBoxPos1, this.boundingBoxPos2);

        for (EntityPlayer player : this.world.playerEntities)
        {
            Dispatcher.sendTo(message, (EntityPlayerMP) player);
        }
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setTag("Left", this.leftClick.serializeNBT());
        tag.setTag("Right", this.rightClick.serializeNBT());

        tag.setTag("BoundingBoxPos1", NBTUtils.vec3dTo(this.boundingBoxPos1));
        tag.setTag("BoundingBoxPos2", NBTUtils.vec3dTo(this.boundingBoxPos2));

        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        if (tag.hasKey("Left"))
        {
            this.leftClick.deserializeNBT(tag.getCompoundTag("Left"));
        }

        if (tag.hasKey("Right"))
        {
            this.rightClick.deserializeNBT(tag.getCompoundTag("Right"));
        }

        if (tag.hasKey("BoundingBoxPos1"))
        {
            this.boundingBoxPos1 = NBTUtils.vec3dFrom(tag.getTag("BoundingBoxPos1"));
        }

        if (tag.hasKey("BoundingBoxPos2"))
        {
            this.boundingBoxPos2 = NBTUtils.vec3dFrom(tag.getTag("BoundingBoxPos2"));
        }
    }

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, this.getBlockMetadata(), this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
    {
        this.readFromNBT(packet.getNbtCompound());
    }
}