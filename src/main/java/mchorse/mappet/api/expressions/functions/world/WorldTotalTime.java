package mchorse.mappet.api.expressions.functions.world;

import mchorse.mclib.math.IValue;
import net.minecraft.world.World;

public class WorldTotalTime extends WorldBaseFunction
{
    public WorldTotalTime(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    public double doubleValue()
    {
        World world = this.getWorld();

        return world == null ? 0 : world.getTotalWorldTime();
    }
}