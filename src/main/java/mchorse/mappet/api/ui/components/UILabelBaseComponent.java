package mchorse.mappet.api.ui.components;

import mchorse.mappet.api.ui.utils.DiscardMethod;
import mchorse.mclib.utils.TextUtils;
import net.minecraft.nbt.NBTTagCompound;

public abstract class UILabelBaseComponent extends UIComponent
{
    public String label = "";

    public UILabelBaseComponent label(String label)
    {
        this.change("Label");

        this.label = label;

        return this;
    }

    @DiscardMethod
    protected String getLabel()
    {
        return TextUtils.processColoredText(this.label);
    }

    @Override
    @DiscardMethod
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setString("Label", this.label);
    }

    @Override
    @DiscardMethod
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Label"))
        {
            this.label = tag.getString("Label");
        }
    }
}