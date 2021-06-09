package mchorse.mappet.api.expressions.functions.world;

import mchorse.mclib.math.IValue;
import net.minecraft.world.World;

public class WorldIsDay extends WorldBaseFunction
{
    public WorldIsDay(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    public double doubleValue()
    {
        World world = this.getWorld();

        if (world == null)
        {
            return 0;
        }

        return world.getWorldTime() % 24000 < 12000 ? 1 : 0;
    }
}