package mchorse.mappet.api.events.nodes;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.EventContext;
import mchorse.mclib.math.IValue;

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
        IValue value = Mappet.expressions.evalute(this.expression, context.subject);

        if (value != null && value.isNumber())
        {
            int result = (int) value.get().doubleValue();

            context.log("Expression \"" + this.expression + "\" is going to switch to its " + result + " execution branch...");

            return result;
        }

        context.log("Switching \"" + this.expression + "\" could not be executed!");

        return this.booleanToExecutionCode(false);
    }
}