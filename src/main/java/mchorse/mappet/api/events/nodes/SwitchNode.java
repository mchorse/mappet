package mchorse.mappet.api.events.nodes;

import mchorse.mappet.api.events.EventContext;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.MathBuilder;

public class SwitchNode extends ConditionNode
{
    public SwitchNode()
    {}

    public SwitchNode(String expression)
    {
        super(expression);
    }

    @Override
    public int execute(EventContext context)
    {
        /* TODO: switch to global expressions */
        MathBuilder builder = new MathBuilder();

        try
        {
            IValue value = builder.parse(this.expression);
            int result = (int) value.get();

            context.log("Expression \"" + this.expression + "\" is going to switch to its " + result + " execution branch...");

            return result;
        }
        catch (Exception e)
        {}

        context.log("Switching \"" + this.expression + "\" could not be executed!");

        return this.booleanToExecutionCode(false);
    }
}