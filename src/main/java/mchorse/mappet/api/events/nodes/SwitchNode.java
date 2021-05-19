package mchorse.mappet.api.events.nodes;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.EventContext;
import mchorse.mclib.math.IValue;
import net.minecraft.nbt.NBTTagCompound;

public class SwitchNode extends EventNode
{
    public String expression = "";

    public SwitchNode()
    {}

    @Override
    public int getColor()
    {
        return 0xf1c40f;
    }

    @Override
    protected String getDisplayTitle()
    {
        return this.expression;
    }

    @Override
    public int execute(EventContext context)
    {
        IValue value = Mappet.expressions.set(context.data).evaluate(this.expression, null);

        if (value != null && value.isNumber())
        {
            int result = 1 + (int) value.get().doubleValue();

            context.log("Expression \"" + this.expression + "\" is going to switch to its " + result + " execution branch...");

            return result;
        }

        context.log("Switching \"" + this.expression + "\" could not be executed!");

        return this.booleanToExecutionCode(false);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        if (!this.expression.isEmpty())
        {
            tag.setString("Expression", this.expression);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Expression"))
        {
            this.expression = tag.getString("Expression");
        }
    }
}