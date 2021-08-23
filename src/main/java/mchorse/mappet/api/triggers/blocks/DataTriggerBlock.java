package mchorse.mappet.api.triggers.blocks;

import mchorse.mappet.api.utils.DataContext;
import net.minecraft.nbt.NBTTagCompound;

public abstract class DataTriggerBlock extends StringTriggerBlock
{
    public String customData = "";

    public DataTriggerBlock()
    {
        super();
    }

    public DataTriggerBlock(String string)
    {
        super(string);
    }

    protected DataContext apply(DataContext context)
    {
        if (!this.customData.isEmpty())
        {
            context = context.copy();
            context.parse(this.customData);
        }

        return context;
    }

    @Override
    protected void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setString("CustomData", this.customData);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.customData = tag.getString("CustomData");
    }
}