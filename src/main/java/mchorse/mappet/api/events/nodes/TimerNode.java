package mchorse.mappet.api.events.nodes;

import mchorse.mappet.api.events.EventContext;
import net.minecraft.client.resources.I18n;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TimerNode extends EventBaseNode
{
    public int timer;

    @Override
    public int execute(EventContext context)
    {
        if (this.timer > 0)
        {
            context.addExecutionFork(this, this.timer);

            return EventBaseNode.HALT;
        }

        return EventBaseNode.ALL;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String getDisplayTitle()
    {
        return I18n.format("mappet.gui.nodes.event.ticks", this.timer);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        tag.setInteger("Timer", this.timer);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Timer"))
        {
            this.timer = tag.getInteger("Timer");
        }
    }
}