package mchorse.mappet.api.ui.components;

import net.minecraft.nbt.NBTTagCompound;

public abstract class UILabelBaseComponent extends UIBaseComponent
{
    public String label = "";

    public UILabelBaseComponent label(String label)
    {
        this.label = label;

        return this;
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setString("Label", this.label);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.label = tag.getString("Label");
    }
}