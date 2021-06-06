package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.utils.EntityUtils;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityBlock extends PropertyBlock
{
    @Override
    protected boolean evaluateBlock(DataContext context)
    {
        Entity entity = this.getEntity(context);

        if (entity == null)
        {
            return false;
        }

        double a = EntityUtils.getProperty(entity, this.id);
        double b = this.value;

        return this.comparison.compare(a, b);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String stringify()
    {
        return this.id + " " + this.comparison.operation.sign + " " + this.value;
    }
}