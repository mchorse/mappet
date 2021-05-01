package mchorse.mappet.api.expressions.functions.player;

import mchorse.mclib.math.IValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.ForgeHooks;

public class PlayerArmor extends PlayerFunction
{
    public PlayerArmor(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    protected double apply(EntityPlayer player)
    {
        return ForgeHooks.getTotalArmorValue(player);
    }
}