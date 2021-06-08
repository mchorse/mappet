package mchorse.mappet.api.triggers.blocks;

import net.minecraft.nbt.NBTTagCompound;

public abstract class StringTriggerBlock extends AbstractTriggerBlock
{
    public String string = "";

    public StringTriggerBlock()
    {}

    public StringTriggerBlock(String string)
    {
        this.string = string;
    }

    @Override
    public boolean isEmpty()
    {
        return this.string.isEmpty();
    }

    @Override
    protected void serializeNBT(NBTTagCompound tag)
    {
        tag.setString(this.getKey(), this.string);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.string = tag.getString(this.getKey());
    }

    protected abstract String getKey();
}