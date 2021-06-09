package mchorse.mappet.api.expressions.functions.world;

import mchorse.mappet.Mappet;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.functions.NNFunction;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public abstract class WorldBaseFunction extends NNFunction
{
    public WorldBaseFunction(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    protected World getWorld()
    {
        World world = Mappet.expressions.getWorld();

        if (this.args.length > 0)
        {
            int dimension = (int) this.getArg(0).doubleValue();

            world = DimensionManager.getWorld(dimension);
        }

        return world;
    }
}