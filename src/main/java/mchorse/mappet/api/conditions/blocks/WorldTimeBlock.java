package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.utils.EnumUtils;
import net.minecraft.nbt.NBTTagCompound;

public class WorldTimeBlock extends AbstractBlock
{
    public TimeCheck check = TimeCheck.DAY;
    public int min;
    public int max = 24000;

    public WorldTimeBlock()
    {}

    @Override
    public int getColor()
    {
        return 0x0088ff;
    }

    @Override
    public String getType()
    {
        return "world_time";
    }

    @Override
    public boolean evaluate(DataContext context)
    {
        long time = context.world.getWorldTime();

        if (this.check == TimeCheck.DAY)
        {
            return time % 24000 < 12000;
        }
        else if (this.check == TimeCheck.NIGHT)
        {
            return time % 24000 >= 12000;
        }
        else if (this.check == TimeCheck.RANGE)
        {
            return time % 24000 >= this.min && time % 24000 <= this.max;
        }

        return false;
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        tag.setInteger("Check", this.check.ordinal());
        tag.setInteger("Min", this.min);
        tag.setInteger("Max", this.max);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.check = EnumUtils.getValue(tag.getInteger("Check"), TimeCheck.values(), TimeCheck.DAY);
        this.min = tag.getInteger("Min");
        this.max = tag.getInteger("Max");
    }

    public static enum TimeCheck
    {
        DAY, NIGHT, RANGE
    }
}