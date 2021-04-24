package mchorse.mappet.api.expressions.functions.inventory;

import mchorse.mclib.math.IValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class InventoryHas extends InventoryFunction
{
    public static boolean compareItemById(String id, ItemStack stack)
    {
        return stack.getItem().getRegistryName().toString().equals(id);
    }

    public static boolean compareInventory(String id, NonNullList<ItemStack> inventory)
    {
        for (ItemStack stack : inventory)
        {
            if (stack.isEmpty())
            {
                continue;
            }

            if (compareItemById(id, stack))
            {
                return true;
            }
        }

        return false;
    }

    public InventoryHas(IValue[] values, String name) throws Exception
    {
        super(values, name);
    }

    @Override
    protected boolean isTrue(String id, EntityPlayer player)
    {
        return compareInventory(id, player.inventory.mainInventory);
    }
}