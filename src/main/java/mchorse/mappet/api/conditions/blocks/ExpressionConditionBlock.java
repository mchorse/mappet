package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.expressions.ExpressionManager;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mclib.math.IValue;
import net.minecraft.nbt.NBTTagCompound;

public class ExpressionConditionBlock extends AbstractConditionBlock
{
    public String expression = "";

    private IValue compiled;

    @Override
    protected boolean evaluateBlock(DataContext context)
    {
        if (this.compiled == null)
        {
            this.compiled = Mappet.expressions.parse(this.expression, ExpressionManager.ZERO);
        }

        return this.compiled.booleanValue();
    }

    @Override
    public String stringify()
    {
        return this.expression;
    }

    @Override
    protected void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setString("Expression", this.expression);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.expression = tag.getString("Expression");
        this.compiled = null;
    }
}