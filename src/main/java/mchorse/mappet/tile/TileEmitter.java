package mchorse.mappet.tile;

import mchorse.mappet.Mappet;
import mchorse.mappet.blocks.BlockEmitter;
import mchorse.mclib.math.IValue;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

public class TileEmitter extends TileEntity implements ITickable
{
    public String expression = "state(\"test\") >= 10";

    private int tick = 0;

    public TileEmitter()
    {}

    @Override
    public void update()
    {
        if (this.tick % 10 == 0 && !this.expression.isEmpty())
        {
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

        this.tick += 1;
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        if (!this.expression.isEmpty())
        {
            tag.setString("Expression", this.expression);
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
    }
}