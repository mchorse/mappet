package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.states.States;
import mchorse.mappet.api.utils.DataContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class StateConditionBlock extends PropertyConditionBlock
{
    public StateConditionBlock()
    {}

    @Override
    public boolean evaluateBlock(DataContext context)
    {
        States states = this.getStates(context);

        if (states == null)
        {
            return false;
        }

        return this.compare(states.get(this.id));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String stringify()
    {
        return this.comparison.stringify(this.id, this.value, this.expression);
    }
}