package mchorse.mappet.tile;

import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.blocks.BlockTrigger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileTrigger extends TileEntity
{
    public Trigger leftClick = new Trigger();
    public Trigger rightClick = new Trigger();

    public void set(NBTTagCompound left, NBTTagCompound right, boolean collidable)
    {
        this.leftClick = new Trigger();
        this.leftClick.deserializeNBT(left);
        this.rightClick = new Trigger();
        this.rightClick.deserializeNBT(right);
        this.world.setBlockState(this.pos, this.world.getBlockState(this.pos).withProperty(BlockTrigger.COLLIDABLE, collidable));

        this.markDirty();
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
    }
}