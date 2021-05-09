package mchorse.mappet.api.events.nodes;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.EventContext;
import mchorse.mclib.math.IValue;
import net.minecraft.nbt.NBTTagCompound;

public class ConditionNode extends EventNode
{
    public String expression = "";

    public ConditionNode()
    {}

    public ConditionNode(String expression)
    {
        this.expression = expression;
    }

    @Override
    public int getColor()
    {
        return 0xff1493;
    }

    @Override
    public int execute(EventContext context)
    {
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

        if (!this.expression.isEmpty())
        {
            tag.setString(this.getKey(), this.expression);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey(this.getKey()))
        {
            this.expression = tag.getString(this.getKey());
        }
    }

    protected String getKey()
    {
        return "Expression";
    }
}