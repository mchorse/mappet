package mchorse.mappet.api.expressions.functions.player;

import mchorse.mclib.math.IValue;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerXp extends PlayerFunction
{
    public PlayerXp(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    protected double apply(EntityPlayer player)
    {
        return player.experienceTotal;
    }
}