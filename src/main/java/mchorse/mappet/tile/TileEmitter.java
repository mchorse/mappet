package mchorse.mappet.tile;

import mchorse.mappet.api.utils.Checker;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.blocks.BlockEmitter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEmitter extends TileEntity implements ITickable
{
    private Checker checker = new Checker();
    private float radius;

    private int tick = 0;

    public TileEmitter()
    {}

    public Checker getChecker()
    {
        return this.checker;
    }

    public float getRadius()
    {
        return this.radius;
    }

    public void setExpression(NBTTagCompound tag, float radius)
    {
        this.checker.deserializeNBT(tag);
        this.radius = radius;
        this.updateExpression();
        this.markDirty();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void update()
    {
        if (this.world.isRemote)
        {
            return;
        }

        /* TODO: add an option to change frequency */
        if (this.tick % 5 == 0 && !this.checker.isEmpty())
        {
            this.updateExpression();
        }

        this.tick += 1;
    }

    private void updateExpression()
    {
        if (this.radius > 0)
        {
            BlockPos pos = this.getPos();
            boolean playerIn = false;

            for (EntityPlayer player : this.world.playerEntities)
            {
                if (player.getDistance(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5) <= this.radius)
                {
                    playerIn = true;

                    break;
                }
            }

            if (!playerIn)
            {
                return;
            }
        }

        IBlockState state = this.world.getBlockState(this.pos);
        boolean result = this.checker.check(new DataContext(this.world));

        if (state.getValue(BlockEmitter.POWERED) != result)
        {
            this.world.setBlockState(this.pos, state.withProperty(BlockEmitter.POWERED, result));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setTag("Checker", this.checker.serializeNBT());

        if (this.radius > 0)
        {
            tag.setFloat("Radius", this.radius);
        }

        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        if (tag.hasKey("Checker"))
        {
            this.checker.deserializeNBT(tag.getCompoundTag("Checker"));
        }

        if (tag.hasKey("Radius"))
        {
            this.radius = tag.getFloat("Radius");
        }
    }
}