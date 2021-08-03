package mchorse.mappet.api.ui;

import mchorse.mappet.api.ui.utils.UIRootComponent;
import mchorse.mappet.api.utils.AbstractData;
import net.minecraft.nbt.NBTTagCompound;

public class UI extends AbstractData
{
    public UIRootComponent root = new UIRootComponent();
    public boolean background;

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setTag("Root", this.root.serializeNBT());
        tag.setBoolean("Background", this.background);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.root.deserializeNBT(tag.getCompoundTag("Root"));
        this.background = tag.getBoolean("Background");
    }
}