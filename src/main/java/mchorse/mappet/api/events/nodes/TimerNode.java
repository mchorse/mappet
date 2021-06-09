package mchorse.mappet.api.events.nodes;

import mchorse.mappet.api.events.EventContext;
import net.minecraft.nbt.NBTTagCompound;

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
    protected String getDisplayTitle()
    {
        return this.timer + " ticks";
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