package mchorse.mappet.api.expressions.functions;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.states.States;
import mchorse.mappet.utils.EntityUtils;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.functions.Function;
import net.minecraft.command.CommandBase;

public class State extends Function
{
    /**
     * Get states repository out of given target
     */
    public static States getState(String target)
    {
        States states = null;

        if (target.equals("~"))
        {
            states = Mappet.states;
        }
        else
        {
            try
            {
                states = EntityUtils.getStates(CommandBase.getEntity(Mappet.expressions.getServer(), Mappet.expressions.getServer(), target));
            }
            catch (Exception e)
            {}
        }

        return states;
    }

    public State(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    public Object getValue()
    {
        String target = this.args.length > 1 ? this.getArg(1).stringValue() : "~";
        States states = getState(target);

        if (states == null)
        {
            return null;
        }

        return states.values.get(this.getArg(0).stringValue());
    }

    @Override
    public int getRequiredArguments()
    {
        return 1;
    }

    @Override
    public IValue get()
    {
        if (this.isNumber())
        {
            this.result.set(this.doubleValue());
        }
        else
        {
            this.result.set(this.stringValue());
        }

        return this.result;
    }

    @Override
    public boolean isNumber()
    {
        return !(this.getValue() instanceof String);
    }

    @Override
    public double doubleValue()
    {
        Object value = this.getValue();

        return value instanceof Number ? ((Number) value).doubleValue() : 0;
    }

    @Override
    public boolean booleanValue()
    {
        return this.getValue() != null;
    }

    @Override
    public String stringValue()
    {
        Object value = this.getValue();

        return value instanceof String ? (String) value : "";
    }
}