package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.utils.AbstractBlock;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.nbt.NBTTagCompound;

public abstract class AbstractConditionBlock extends AbstractBlock
{
    public boolean not;
    public boolean or;

    public boolean evaluate(DataContext context)
    {
        boolean result = this.evaluateBlock(context);

        return this.not != result;
    }

    protected abstract boolean evaluateBlock(DataContext context);

    @Override
    protected void serializeNBT(NBTTagCompound tag)
    {
        tag.setBoolean("Not", this.not);
        tag.setBoolean("Or", this.or);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.not = tag.getBoolean("Not");
        this.or = tag.getBoolean("Or");
    }
}