package mchorse.mappet.api.expressions.functions.entity;

import mchorse.mappet.Mappet;
import mchorse.mappet.utils.EntityUtils;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.functions.SNFunction;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;

public class EntityFunction extends SNFunction
{
    public EntityFunction(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    public int getRequiredArguments()
    {
        return 2;
    }

    @Override
    public double doubleValue()
    {
        try
        {
            String property = this.getArg(0).stringValue();
            String target = this.getArg(1).stringValue();
            Entity entity = CommandBase.getEntity(Mappet.expressions.getServer(), Mappet.expressions.getServer(), target);

            return EntityUtils.getProperty(entity, property);
        }
        catch (Exception e)
        {}

        return 0;
    }
}