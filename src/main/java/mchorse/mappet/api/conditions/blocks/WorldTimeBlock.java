package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.utils.EnumUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class WorldTimeBlock extends AbstractBlock
{
    public TimeCheck check = TimeCheck.DAY;
    public int min;
    public int max = 24000;

    public WorldTimeBlock()
    {}

    @Override
    public boolean evaluateBlock(DataContext context)
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
    @SideOnly(Side.CLIENT)
    public String stringify()
    {
        String label = this.check.stringify();

        if (this.check == TimeCheck.RANGE)
        {
            label += " " + this.min + "-" + this.max;
        }

        return label;
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
        DAY, NIGHT, RANGE;

        @SideOnly(Side.CLIENT)
        public String stringify()
        {
            return I18n.format(this.getKey());
        }

        public String getKey()
        {
            return "mappet.gui.conditions.world_time.types." + this.name().toLowerCase();
        }
    }
}