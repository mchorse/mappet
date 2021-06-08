package mchorse.mappet.api.triggers.blocks;

import mchorse.mappet.api.utils.DataContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class AbstractTriggerBlock implements INBTSerializable<NBTTagCompound>
{
    public int delay = 1;

    private int tick;

    public void triggerWithDelay(DataContext context)
    {
        this.tick += 1;

        if (this.tick > 0 && this.tick % this.delay == 0)
        {
            this.trigger(context);
            this.tick = 0;
        }
    }

    public abstract void trigger(DataContext context);

    public abstract boolean isEmpty();

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("Delay", this.delay);

        this.serializeNBT(tag);

        return tag;
    }

    protected abstract void serializeNBT(NBTTagCompound tag);

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.delay = tag.getInteger("Delay");
    }
}