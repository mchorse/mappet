package mchorse.mappet.api.events.hotkeys;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.expressions.ExpressionManager;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mclib.math.IValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class EventHotkey implements INBTSerializable<NBTTagCompound>
{
    public int keycode;
    public String event = "";
    public String enabled = "";

    private IValue value;

    public void execute(EntityPlayer player)
    {
        if (this.isEnabled(player))
        {
            Mappet.events.execute(this.event, new EventContext(new DataContext(player)));
        }
    }

    private boolean isEnabled(EntityPlayer player)
    {
        if (this.enabled.isEmpty())
        {
            return true;
        }

        if (this.value == null)
        {
            this.value = Mappet.expressions.evaluate(this.enabled, ExpressionManager.ONE);
        }

        Mappet.expressions.set(player);

        return this.value.booleanValue();
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("Keycode", this.keycode);
        tag.setString("Event", this.event);
        tag.setString("Enabled", this.enabled);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.keycode = tag.getInteger("Keycode");
        this.event = tag.getString("Event");
        this.enabled = tag.getString("Enabled");
    }
}