package mchorse.mappet.api.ui;

import mchorse.mappet.api.ui.utils.UIRootComponent;
import mchorse.mappet.api.utils.AbstractData;
import net.minecraft.nbt.NBTTagCompound;

public class UI extends AbstractData
{
    public UIRootComponent root = new UIRootComponent();

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setTag("Root", this.root.serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.root.deserializeNBT(tag.getCompoundTag("Root"));
    }
}