package mchorse.mappet.api.expressions.functions;

import mchorse.mappet.Mappet;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.functions.SNFunction;
import net.minecraft.entity.player.EntityPlayer;

public class InventoryArmor extends SNFunction
{
    public InventoryArmor(IValue[] values, String name) throws Exception
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

            if (InventoryHas.compareInventory(id, subject.inventory.armorInventory))
            {
                return 1;
            }
        }

        return 0;
    }
}