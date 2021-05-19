package mchorse.mappet.api.utils;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.conditions.Condition;
import mchorse.mappet.api.expressions.ExpressionManager;
import mchorse.mappet.utils.EnumUtils;
import mchorse.mclib.math.IValue;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

public class Checker implements INBTSerializable<NBTTagCompound>
{
    public String expression = "";
    public Condition condition;
    public Mode mode = Mode.CONDITION;

    private IValue value;
    private boolean defaultValue;

    public Checker()
    {
        this(false);
    }

    public Checker(boolean defaultValue)
    {
        this.defaultValue = defaultValue;
        this.condition = new Condition(defaultValue);
    }

    public boolean check(DataContext data)
    {
        if (this.isEmpty())
        {
            return this.defaultValue;
        }

        if (this.mode == Mode.CONDITION)
        {
            return this.condition.execute(data);
        }

        if (this.value == null)
        {
            this.value = Mappet.expressions.parse(this.expression, this.defaultValue ? ExpressionManager.ONE : ExpressionManager.ZERO);
        }

        Mappet.expressions.set(data);

        return this.value.booleanValue();
    }

    public boolean isEmpty()
    {
        if (this.mode == Mode.CONDITION)
        {
            return this.condition.blocks.isEmpty();
        }

        return this.expression.isEmpty();
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("Mode", this.mode.ordinal());

        if (!this.expression.isEmpty())
        {
            tag.setString("Expression", this.expression);
        }

        tag.setTag("Condition", this.condition.serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.value = null;

        if (tag.hasKey("Mode", Constants.NBT.TAG_ANY_NUMERIC))
        {
            this.mode = EnumUtils.getValue(tag.getInteger("Mode"), Mode.values(), Mode.CONDITION);
        }

        if (tag.hasKey("Expression"))
        {
            this.expression = tag.getString("Expression");
        }

        if (tag.hasKey("Condition"))
        {
            this.condition.deserializeNBT(tag.getCompoundTag("Condition"));
        }
    }

    public static enum Mode
    {
        EXPRESSION, CONDITION
    }
}