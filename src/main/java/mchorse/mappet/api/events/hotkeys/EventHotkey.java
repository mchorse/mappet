package mchorse.mappet.api.events.hotkeys;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.utils.Checker;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class EventHotkey implements INBTSerializable<NBTTagCompound>
{
    public int keycode;
    public String event = "";
    public Checker enabled = new Checker(true);

    public void execute(EntityPlayer player)
    {
        if (this.isEnabled(player))
        {
            Mappet.events.execute(this.event, new EventContext(new DataContext(player)));
        }
    }

    private boolean isEnabled(EntityPlayer player)
    {
        return this.enabled.check(new DataContext(player));
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("Keycode", this.keycode);
        tag.setString("Event", this.event);
        tag.setTag("Enabled", this.enabled.serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.keycode = tag.getInteger("Keycode");
        this.event = tag.getString("Event");
        this.enabled.deserializeNBT(tag.getTag("Enabled"));
    }
}