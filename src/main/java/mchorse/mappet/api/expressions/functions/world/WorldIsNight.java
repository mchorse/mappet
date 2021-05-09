package mchorse.mappet.api.expressions.functions.world;

import mchorse.mappet.Mappet;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.functions.NNFunction;

public class WorldIsNight extends NNFunction
{
    public WorldIsNight(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    public double doubleValue()
    {
        return Mappet.expressions.getWorld().getWorldTime() % 24000 > 12000 ? 1 : 0;
    }
}