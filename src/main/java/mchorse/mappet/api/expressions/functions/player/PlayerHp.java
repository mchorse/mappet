package mchorse.mappet.api.expressions.functions.player;

import mchorse.mclib.math.IValue;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerHp extends PlayerFunction
{
    public PlayerHp(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    protected double apply(EntityPlayer player)
    {
        return player.getHealth();
    }
}