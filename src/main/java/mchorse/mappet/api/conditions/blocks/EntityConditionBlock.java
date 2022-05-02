package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.utils.ComparisonMode;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.TargetMode;
import mchorse.mappet.utils.EntityUtils;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class EntityConditionBlock extends PropertyConditionBlock
{
    @Override
    protected TargetMode getDefaultTarget()
    {
        return TargetMode.SUBJECT;
    }

    @Override
    protected boolean evaluateBlock(DataContext context)
    {
        Entity entity = this.target.getEntity(context);

        if (entity == null)
        {
            return false;
        }

        double value = EntityUtils.getProperty(entity, this.id);

        if (
            this.comparison.comparison == ComparisonMode.EQUALS_TO_STRING ||
            this.comparison.comparison == ComparisonMode.CONTAINS_STRING ||
            this.comparison.comparison == ComparisonMode.REGEXP_STRING
        ) {
            return this.compareString(String.valueOf(value));
        }

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

        return this.comparison.stringify(id);
    }
}