package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.api.conditions.utils.Comparison;
import mchorse.mappet.api.expressions.ExpressionManager;
import mchorse.mappet.utils.EnumUtils;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.MathBuilder;
import mchorse.mclib.math.Variable;
import net.minecraft.nbt.NBTTagCompound;

public abstract class PropertyConditionBlock extends TargetConditionBlock
{
    private static final MathBuilder MATH;
    private static final Variable VALUE;
    private static final Variable VALUE2;

    public Comparison comparison = Comparison.EQUALS;
    public double value;
    public String expression = "";

    private IValue compiledValue;

    static
    {
        VALUE = new Variable("value", 0);
        VALUE2 = new Variable("x", 0);

        MATH = new MathBuilder();
        MATH.register(VALUE);
        MATH.register(VALUE2);
    }

    /**
     * Compare given value to expression or comparison mode
     */
    protected boolean compare(double a)
    {
        if (this.comparison == Comparison.EXPRESSION)
        {
            if (this.compiledValue == null)
            {
                try
                {
                    this.compiledValue = MATH.parse(this.expression);
                }
                catch (Exception e)
                {
                    this.compiledValue = ExpressionManager.ZERO;
                }
            }

            VALUE.set(a);
            VALUE2.set(a);

            return this.compiledValue.booleanValue();
        }

        return this.comparison.compare(a, this.value);
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setInteger("Comparison", this.comparison.ordinal());
        tag.setDouble("Value", this.value);
        tag.setString("Expression", this.expression);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.compiledValue = null;

        this.comparison = EnumUtils.getValue(tag.getInteger("Comparison"), Comparison.values(), Comparison.EQUALS);
        this.value = tag.getDouble("Value");
        this.expression = tag.getString("Expression");
    }
}
