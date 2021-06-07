package mchorse.mappet.api.misc.hotkeys;

import mchorse.mappet.api.utils.Checker;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.Trigger;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class TriggerHotkey implements INBTSerializable<NBTTagCompound>
{
    public int keycode;
    public Trigger trigger = new Trigger();
    public Checker enabled = new Checker(true);

    public void execute(DataContext context)
    {
        if (this.isEnabled(context))
        {
            this.trigger.trigger(context);
        }
    }

    private boolean isEnabled(DataContext context)
    {
        return this.enabled.check(context);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("Keycode", this.keycode);
        tag.setTag("Trigger", this.trigger.serializeNBT());
        tag.setTag("Enabled", this.enabled.serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.keycode = tag.getInteger("Keycode");
        this.trigger.deserializeNBT(tag.getCompoundTag("Trigger"));
        this.enabled.deserializeNBT(tag.getTag("Enabled"));
    }
}