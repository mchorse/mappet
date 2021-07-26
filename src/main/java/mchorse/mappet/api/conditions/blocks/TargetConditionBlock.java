package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.utils.Target;
import mchorse.mappet.api.utils.TargetMode;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TargetConditionBlock extends AbstractConditionBlock
{
    public String id = "";
    public Target target = new Target(this.getDefaultTarget());

    protected TargetMode getDefaultTarget()
    {
        return TargetMode.GLOBAL;
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setString("Id", this.id.trim());
        tag.merge(this.target.serializeNBT());
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.id = tag.getString("Id");
        this.target.deserializeNBT(tag);
    }
}