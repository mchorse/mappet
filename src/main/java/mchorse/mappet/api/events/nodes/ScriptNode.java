package mchorse.mappet.api.events.nodes;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.EventContext;
import net.minecraft.nbt.NBTTagCompound;

public class ScriptNode extends DataNode
{
    public String function = "";

    @Override
    protected String getDisplayTitle()
    {
        return super.getDisplayTitle() + (this.function.isEmpty() ? "" : " (§7" + this.function + "§r)");
    }

    @Override
    public int execute(EventContext context)
    {
        boolean result = true;

        try
        {
            Mappet.scripts.execute(this.dataId, this.function.trim(), this.apply(context));
        }
        catch (Exception e)
        {
            result = false;
        }

        return this.booleanToExecutionCode(result);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        tag.setString("Function", this.function);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.function = tag.getString("Function");
    }
}