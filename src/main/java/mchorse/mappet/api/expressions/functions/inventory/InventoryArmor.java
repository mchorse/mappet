package mchorse.mappet.api.expressions.functions.inventory;

import mchorse.mclib.math.IValue;
import net.minecraft.entity.player.EntityPlayer;

public class InventoryArmor extends InventoryFunction
{
    public InventoryArmor(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    protected boolean isTrue(String id, EntityPlayer player)
    {
        return InventoryHas.compareInventory(id, player.inventory.armorInventory);
    }
}