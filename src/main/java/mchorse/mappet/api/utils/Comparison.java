package mchorse.mappet.api.utils;

import mchorse.mappet.api.expressions.ExpressionManager;
import mchorse.mappet.utils.EnumUtils;
import mchorse.mclib.math.IValue;
import mchorse.mclib.math.MathBuilder;
import mchorse.mclib.math.Variable;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Comparison implements INBTSerializable<NBTTagCompound>
{
    private static final MathBuilder MATH;
    private static final Variable VALUE;
    private static final Variable VALUE2;

    public ComparisonMode comparison = ComparisonMode.EQUALS;
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
    public boolean compare(double a)
    {
        if (this.comparison == ComparisonMode.EXPRESSION)
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

    public boolean compareString(String a)
    {
        if (this.comparison == ComparisonMode.EQUALS_TO_STRING)
        {
            return a.equals(this.expression);
        }

        if (this.comparison == ComparisonMode.CONTAINS_STRING)
        {
            return a.contains(this.expression);
        }

        if (this.comparison == ComparisonMode.REGEXP_STRING)
        {
            return a.matches(this.expression);
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public String stringify(String id)
    {
        return this.comparison.stringify(id, this.value, this.expression);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("Comparison", this.comparison.ordinal());
        tag.setDouble("Value", this.value);
        tag.setString("Expression", this.expression);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.compiledValue = null;

        this.comparison = EnumUtils.getValue(tag.getInteger("Comparison"), ComparisonMode.values(), ComparisonMode.EQUALS);
        this.value = tag.getDouble("Value");
        this.expression = tag.getString("Expression");
    }
}