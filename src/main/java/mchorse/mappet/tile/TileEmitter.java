package mchorse.mappet.tile;

import mchorse.mappet.Mappet;
import mchorse.mappet.blocks.BlockEmitter;
import mchorse.mclib.math.IValue;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TileEmitter extends TileEntity implements ITickable
{
    private String expression = "";
    private float radius;

    private int tick = 0;

    public TileEmitter()
    {}

    public String getExpression()
    {
        return this.expression;
    }

    public float getRadius()
    {
        return this.radius;
    }

    public void setExpression(String expression, float radius)
    {
        this.expression = expression;
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

        /* TODO: rewrite to use state changes */
        if (this.tick % 5 == 0 && !this.expression.isEmpty())
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

        IValue value = Mappet.expressions.evalute(this.expression, null);

        if (value != null)
        {
            IBlockState state = this.world.getBlockState(this.pos);
            boolean result = value.booleanValue();

            if (state.getValue(BlockEmitter.POWERED) != result)
            {
                this.world.setBlockState(this.pos, state.withProperty(BlockEmitter.POWERED, result));
            }
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        if (!this.expression.isEmpty())
        {
            tag.setString("Expression", this.expression);
        }

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

        if (tag.hasKey("Expression"))
        {
            this.expression = tag.getString("Expression");
        }

        if (tag.hasKey("Radius"))
        {
            this.radius = tag.getFloat("Radius");
        }
    }
}