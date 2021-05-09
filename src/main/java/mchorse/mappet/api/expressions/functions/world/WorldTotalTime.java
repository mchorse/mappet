package mchorse.mappet.api.expressions.functions.world;

import mchorse.mappet.Mappet;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.functions.NNFunction;

public class WorldTotalTime extends NNFunction
{
    public WorldTotalTime(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    public double doubleValue()
    {
        return Mappet.expressions.getWorld().getTotalWorldTime();
    }
}