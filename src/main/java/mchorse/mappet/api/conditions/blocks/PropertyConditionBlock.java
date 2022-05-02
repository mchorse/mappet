package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.utils.Comparison;
import net.minecraft.nbt.NBTTagCompound;

public abstract class PropertyConditionBlock extends TargetConditionBlock
{
    public Comparison comparison = new Comparison();

    /**
     * Compare given value to expression or comparison mode
     */
    protected boolean compare(double a)
    {
        return this.comparison.compare(a);
    }

    protected boolean compareString(String a)
    {
        return this.comparison.compareString(a);
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.merge(this.comparison.serializeNBT());
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.comparison.deserializeNBT(tag);
    }
}
