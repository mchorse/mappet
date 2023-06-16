package mchorse.mappet.api.triggers.blocks;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.utils.AbstractBlock;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class AbstractTriggerBlock extends AbstractBlock
{
    public int frequency = 1;

    private int tick;

    @Override
    @SideOnly(Side.CLIENT)
    public String stringify()
    {
        return I18n.format("mappet.gui.trigger_types." + CommonProxy.getTriggerBlocks().getType(this));
    }

    public void triggerWithFrequency(DataContext context)
    {
        this.tick += 1;

        if (this.tick > 0 && this.tick % Math.max(this.frequency, 1) == 0)
        {
            this.trigger(context);
            this.tick = 0;
        }
    }

    public abstract void trigger(DataContext context);

    public abstract boolean isEmpty();

    @Override
    protected void serializeNBT(NBTTagCompound tag)
    {
        tag.setInteger("Frequency", this.frequency);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        /* Backward compatibility with rc3 and below */
        if (tag.hasKey("Delay"))
        {
            tag.setTag("Frequency", tag.getTag("Delay"));
        }

        this.frequency = tag.getInteger("Frequency");
    }

    public NBTTagCompound toNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        this.serializeNBT(tag);
        return tag;
    }
}