package mchorse.mappet.api.expressions.functions.player;

import mchorse.mclib.math.IValue;
import net.minecraft.entity.player.EntityPlayer;

public class PlayerXpLevel extends PlayerFunction
{
    public PlayerXpLevel(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    protected double apply(EntityPlayer player)
    {
        return player.experienceLevel;
    }
}