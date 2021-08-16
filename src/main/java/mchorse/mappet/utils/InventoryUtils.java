package mchorse.mappet.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class InventoryUtils
{
    public static boolean areStacksSimilar(ItemStack a, ItemStack b)
    {
        return areStacksSimilar(a, b, false);
    }

    public static boolean areStacksSimilar(ItemStack a, ItemStack b, boolean ignoreNBT)
    {
        if (a.isEmpty() && b.isEmpty())
        {
            return true;
        }

        if (ignoreNBT)
        {
            return a.getItem() == b.getItem();
        }

        return ItemStack.areItemsEqualIgnoreDurability(a, b) && ItemStack.areItemStackTagsEqual(a, b);
    }

    public static int countItems(EntityPlayer player, ItemStack target)
    {
        return countItems(player, target, true, false);
    }

    public static int countItems(EntityPlayer player, ItemStack target, boolean stopUponTargetCount, boolean ignoreNBT)
    {
        int count = 0;

        for (int i = 0, c = player.inventory.getSizeInventory(); i < c; i ++)
        {
            ItemStack stack = player.inventory.getStackInSlot(i);

            if (areStacksSimilar(target, stack, ignoreNBT))
            {
                count += stack.getCount();

                if (stopUponTargetCount && count >= target.getCount())
                {
                    return count;
                }
            }
        }

        return count;
    }

    public static int countItems(NonNullList<ItemStack> inventory, ItemStack target, boolean stopUponTargetCount)
    {
        int count = 0;

        for (int i = 0, c = inventory.size(); i < c; i ++)
        {
            ItemStack stack = inventory.get(i);

            if (areStacksSimilar(target, stack))
            {
                count += stack.getCount();

                if (stopUponTargetCount && count >= target.getCount())
                {
                    return count;
                }
            }
        }

        return count;
    }
}
