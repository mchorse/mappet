package mchorse.mappet.api.expressions.functions;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.states.States;
import mchorse.mappet.utils.WorldUtils;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.functions.SNFunction;
import net.minecraft.command.CommandBase;

public class State extends SNFunction
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
                states = WorldUtils.getStates(CommandBase.getEntity(Mappet.expressions.getServer(), Mappet.expressions.getServer(), target));
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

    @Override
    public int getRequiredArguments()
    {
        return 1;
    }

    @Override
    public double doubleValue()
    {
        String target = this.args.length > 1 ? this.getArg(1).stringValue() : "~";
        States states = getState(target);

        if (states == null)
        {
            return 0;
        }

        return states.get(this.getArg(0).stringValue());
    }
}