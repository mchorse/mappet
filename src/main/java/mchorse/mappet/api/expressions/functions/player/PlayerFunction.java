package mchorse.mappet.api.expressions.functions.player;

import mchorse.mappet.Mappet;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.functions.SNFunction;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public abstract class PlayerFunction extends SNFunction
{
    public PlayerFunction(IValue[] values, String name) throws Exception
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
        try
        {
            String target = this.getArg(0).stringValue();
            EntityPlayerMP player = CommandBase.getPlayer(Mappet.expressions.getServer(), Mappet.expressions.getServer(), target);

            return this.apply(player);
        }
        catch (Exception e)
        {}

        return 0;
    }

    protected abstract double apply(EntityPlayer player);
}