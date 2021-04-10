package mchorse.mappet.api.events.nodes;

import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.utils.nodes.Node;
import net.minecraft.nbt.NBTTagCompound;

public abstract class EventNode extends Node
{
    public static final int HALT = -1;
    public static final int ALL = 0;

    /**
     * When true, this node will return 1 or 2 depending on the
     * success of the node, as opposed to none or all
     */
    public boolean binary;

    /**
     * Executes this node, and depending on return value:
     * -1 (or below) = don't execute children nodes
     * 0             = execute all children nodes
     * 1 (or above)  = execute only the give node minus one
     */
    public abstract int execute(EventContext context);

    protected int booleanToExecutionCode(boolean result)
    {
        if (this.binary)
        {
            /* Based on the result execute either 1st or 2nd node */
            return result ? 1 : 2;
        }

        return result ? ALL : HALT;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        if (this.binary)
        {
            tag.setBoolean("Binary", this.binary);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Binary"))
        {
            this.binary = tag.getBoolean("Binary");
        }
    }
}