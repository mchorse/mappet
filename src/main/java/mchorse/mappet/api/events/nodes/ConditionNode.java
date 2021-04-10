package mchorse.mappet.api.events.nodes;

import mchorse.mappet.api.events.EventContext;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.MathBuilder;
import mchorse.mclib.math.Operation;
import net.minecraft.nbt.NBTTagCompound;

public class ConditionNode extends EventNode
{
    public String condition = "";

    public ConditionNode()
    {}

    public ConditionNode(String condition)
    {
        this.condition = condition;
    }

    @Override
    public boolean execute(EventContext context)
    {
        MathBuilder builder = new MathBuilder();

        try
        {
            IValue value = builder.parse(this.condition);
            boolean result = !Operation.equals(value.get(), 0);

            context.log("The result \"" + this.condition + "\" is " + (result ? "true" : "false"));

            return result;
        }
        catch (Exception e)
        {}

        context.log("Condition \"" + this.condition + "\" could not be executed!");

        return false;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        if (!this.condition.isEmpty())
        {
            tag.setString("Condition", this.condition);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Condition"))
        {
            this.condition = tag.getString("Condition");
        }
    }
}