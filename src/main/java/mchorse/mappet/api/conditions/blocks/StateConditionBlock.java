package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.states.States;
import mchorse.mappet.api.utils.DataContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mchorse.mappet.api.utils.ComparisonMode;

public class StateConditionBlock extends PropertyConditionBlock
{
    public StateConditionBlock()
    {}

    @Override
    public boolean evaluateBlock(DataContext context)
    {
        States states = this.target.getStates(context);

        if (states == null)
        {
            return false;
        }

        if (this.comparison.comparison == ComparisonMode.EQUALS_TO_STRING)
        {
            if (states.isString(this.id))
            {
                return states.getString(this.id).equals(this.comparison.expression);
            }
            else
            {
                return false;
            }
        }

        return this.compare(states.getNumber(this.id));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String stringify()
    {
        return this.comparison.stringify(this.id);
    }
}