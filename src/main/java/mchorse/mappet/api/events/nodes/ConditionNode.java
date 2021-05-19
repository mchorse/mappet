package mchorse.mappet.api.events.nodes;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.conditions.Condition;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.utils.EnumUtils;
import mchorse.mclib.math.IValue;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

public class ConditionNode extends EventNode
{
    public ConditionMode mode = ConditionMode.CONDITION;
    public String expression = "";
    public Condition condition = new Condition();

    public ConditionNode()
    {}

    @Override
    public int getColor()
    {
        return 0xff1493;
    }

    @Override
    protected String getDisplayTitle()
    {
        return this.mode == ConditionMode.CONDITION ? "" : this.expression;
    }

    @Override
    public int execute(EventContext context)
    {
        if (this.mode == ConditionMode.CONDITION)
        {
            boolean result = this.condition.execute(context.data);

            context.log("The result of condition is " + (result ? "true" : "false"));

            return this.booleanToExecutionCode(result);
        }

        IValue value = Mappet.expressions.set(context.data).evaluate(this.expression, null);

        if (value != null)
        {
            boolean result = value.booleanValue();

            context.log("The result \"" + this.expression + "\" is " + (result ? "true" : "false"));

            return this.booleanToExecutionCode(result);
        }

        context.log("Condition \"" + this.expression + "\" could not be executed!");

        return this.booleanToExecutionCode(false);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

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
        super.deserializeNBT(tag);

        if (tag.hasKey("Mode", Constants.NBT.TAG_ANY_NUMERIC))
        {
            this.mode = EnumUtils.getValue(tag.getInteger("Mode"), ConditionMode.values(), ConditionMode.CONDITION);
        }
        else
        {
            this.mode = ConditionMode.EXPRESSION;
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

    public static enum ConditionMode
    {
        EXPRESSION, CONDITION
    }
}