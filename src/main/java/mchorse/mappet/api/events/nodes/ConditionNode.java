package mchorse.mappet.api.events.nodes;

import mchorse.mappet.api.events.EventContext;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.MathBuilder;
import mchorse.mclib.math.Operation;
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
    public int execute(EventContext context)
    {
        /* TODO: switch to global expressions */
        MathBuilder builder = new MathBuilder();

        try
        {
            IValue value = builder.parse(this.expression);
            boolean result = !Operation.equals(value.get(), 0);

            context.log("The result \"" + this.expression + "\" is " + (result ? "true" : "false"));

            return this.booleanToExecutionCode(result);
        }
        catch (Exception e)
        {}

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