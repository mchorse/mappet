package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.conditions.utils.Comparison;
import mchorse.mappet.utils.EnumUtils;
import net.minecraft.nbt.NBTTagCompound;

public abstract class PropertyBlock extends TargetBlock
{
    public Comparison comparison = Comparison.EQUALS;
    public double value;

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setInteger("Comparison", this.comparison.ordinal());
        tag.setDouble("Value", this.value);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.comparison = EnumUtils.getValue(tag.getInteger("Comparison"), Comparison.values(), Comparison.EQUALS);
        this.value = tag.getDouble("Value");
    }
}
