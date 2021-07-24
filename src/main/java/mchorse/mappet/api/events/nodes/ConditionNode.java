package mchorse.mappet.api.events.nodes;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.conditions.Checker;
import mchorse.mappet.api.events.EventContext;
import mchorse.mclib.math.IValue;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ConditionNode extends EventBaseNode
{
    public Checker condition = new Checker();

    public ConditionNode()
    {}

    @Override
    @SideOnly(Side.CLIENT)
    protected String getDisplayTitle()
    {
        return this.condition.mode == Checker.Mode.CONDITION ? "" : this.condition.expression;
    }

    @Override
    public int execute(EventContext context)
    {
        if (this.condition.mode == Checker.Mode.CONDITION)
        {
            boolean result = this.condition.condition.execute(context.data);

            context.log("The result of condition is " + (result ? "true" : "false"));

            return this.booleanToExecutionCode(result);
        }

        String expression = this.condition.expression;
        IValue value = Mappet.expressions.set(context.data).parse(expression, null);

        if (value != null)
        {
            boolean result = value.booleanValue();

            context.log("The result \"" + expression + "\" is " + (result ? "true" : "false"));

            return this.booleanToExecutionCode(result);
        }

        context.log("Condition \"" + expression + "\" could not be executed!");

        return this.booleanToExecutionCode(false);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        tag.setTag("Condition", this.condition.serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Condition"))
        {
            this.condition.deserializeNBT(tag.getTag("Condition"));
        }
    }
}