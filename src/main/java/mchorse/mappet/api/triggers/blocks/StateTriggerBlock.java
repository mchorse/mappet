package mchorse.mappet.api.triggers.blocks;

import mchorse.mappet.api.states.States;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.Target;
import mchorse.mappet.api.utils.TargetMode;
import mchorse.mappet.utils.EnumUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

public class StateTriggerBlock extends StringTriggerBlock
{
    public Target target = new Target(TargetMode.GLOBAL);
    public StateMode mode = StateMode.SET;
    public Object value = 0D;

    @Override
    public void trigger(DataContext context)
    {
        States states = this.target.getStates(context);

        if (states == null)
        {
            return;
        }

        if (this.mode == StateMode.ADD && this.value instanceof Number)
        {
            states.add(this.string, ((Number) this.value).doubleValue());
        }
        else if (this.mode == StateMode.SET)
        {
            if (this.value instanceof Number)
            {
                states.setNumber(this.string, ((Number) this.value).doubleValue());
            }
            else if (this.value instanceof String)
            {
                states.setString(this.string, (String) this.value);
            }
        }
        else
        {
            states.maskedReset(this.string);
        }
    }

    @Override
    protected String getKey()
    {
        return "State";
    }

    @Override
    protected void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setTag("Target", this.target.serializeNBT());
        tag.setInteger("Mode", this.mode.ordinal());

        if (this.value instanceof Number)
        {
            tag.setDouble("Value", ((Number) this.value).doubleValue());
        }
        else if (this.value instanceof String)
        {
            tag.setString("Value", (String) this.value);
        }
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.target.deserializeNBT(tag.getCompoundTag("Target"));
        this.mode = EnumUtils.getValue(tag.getInteger("Mode"), StateMode.values(), StateMode.SET);

        if (tag.hasKey("Value", Constants.NBT.TAG_ANY_NUMERIC))
        {
            this.value = tag.getDouble("Value");
        }
        else if (tag.hasKey("Value", Constants.NBT.TAG_STRING))
        {
            this.value = tag.getString("Value");
        }
    }

    public static enum StateMode
    {
        ADD, SET, REMOVE
    }
}