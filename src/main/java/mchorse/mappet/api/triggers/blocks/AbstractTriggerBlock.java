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
    public int delay = 1;

    private int tick;

    @Override
    @SideOnly(Side.CLIENT)
    public String stringify()
    {
        return I18n.format("mappet.gui.trigger_types." + CommonProxy.getTriggerBlocks().getType(this));
    }

    public void triggerWithDelay(DataContext context)
    {
        this.tick += 1;

        if (this.tick > 0 && this.tick % Math.max(this.delay, 1) == 0)
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
        tag.setInteger("Delay", this.delay);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.delay = tag.getInteger("Delay");
    }
}