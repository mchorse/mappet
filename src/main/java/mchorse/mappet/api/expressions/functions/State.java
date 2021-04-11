package mchorse.mappet.api.expressions.functions;

import mchorse.mappet.Mappet;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.functions.SNFunction;

public class State extends SNFunction
{
    public State(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    public int getRequiredArguments()
    {
        return 1;
    }

    @Override
    public double doubleValue()
    {
        return Mappet.states.get(this.getArg(0).stringValue());
    }
}