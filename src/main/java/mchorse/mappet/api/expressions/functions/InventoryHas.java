package mchorse.mappet.api.expressions.functions;

import mchorse.mappet.Mappet;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.functions.SNFunction;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class InventoryHas extends SNFunction
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
    public int getRequiredArguments()
    {
        return 1;
    }

    @Override
    public double doubleValue()
    {
        if (Mappet.expressions.subject instanceof EntityPlayer)
        {
            EntityPlayer subject = (EntityPlayer) Mappet.expressions.subject;
            String id = this.getArg(0).stringValue();

            if (compareInventory(id, subject.inventory.mainInventory))
            {
                return 1;
            }
        }

        return 0;
    }
}