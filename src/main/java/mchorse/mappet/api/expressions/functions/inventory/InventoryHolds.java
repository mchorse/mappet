package mchorse.mappet.api.expressions.functions.inventory;

import mchorse.mclib.math.IValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;

public class InventoryHolds extends InventoryFunction
{
    public InventoryHolds(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    protected boolean isTrue(String id, EntityPlayer player)
    {
        return InventoryHas.compareItemById(id, player.getItemStackFromSlot(EntityEquipmentSlot.MAINHAND)) || InventoryHas.compareItemById(id, player.getItemStackFromSlot(EntityEquipmentSlot.OFFHAND));
    }
}