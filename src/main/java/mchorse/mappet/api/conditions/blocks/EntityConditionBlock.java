package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.conditions.utils.Target;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.utils.EntityUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityConditionBlock extends PropertyConditionBlock
{
    @Override
    protected Target getDefaultTarget()
    {
        return Target.SUBJECT;
    }

    @Override
    protected boolean evaluateBlock(DataContext context)
    {
        Entity entity = this.getEntity(context);

        if (entity == null)
        {
            return false;
        }

        double value = EntityUtils.getProperty(entity, this.id);

        return this.compare(value);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String stringify()
    {
        String id = "";

        if (EntityUtils.ENTITY_PROPERTIES.contains(this.id))
        {
            id = I18n.format("mappet.gui.entity_property." + this.id);
        }

        return this.comparison.stringify(id, this.value, this.expression);
    }
}