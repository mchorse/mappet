package mchorse.mappet.api.conditions.utils;

import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.math.Operation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum Comparison
{
    LESS(Operation.LESS), LESS_THAN(Operation.LESS_THAN), EQUALS(Operation.EQUALS), GREATER_THAN(Operation.GREATER_THAN), GREATER(Operation.GREATER),
    IS_TRUE(null)
    {
        @Override
        public boolean compare(double a, double b)
        {
            return Operation.isTrue(a);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public String stringify(String a, double b)
        {
            return a + " == true";
        }

        @Override
        @SideOnly(Side.CLIENT)
        public IKey stringify()
        {
            return IKey.lang("mappet.gui.conditions.comparisons.is_true");
        }
    },
    IS_FALSE(null)
    {
        @Override
        public boolean compare(double a, double b)
        {
            return !Operation.isTrue(a);
        }

        @Override
        @SideOnly(Side.CLIENT)
        public String stringify(String a, double b)
        {
            return a + " == false";
        }

        @Override
        @SideOnly(Side.CLIENT)
        public IKey stringify()
        {
            return IKey.lang("mappet.gui.conditions.comparisons.is_false");
        }
    };

    public final Operation operation;

    private Comparison(Operation operation)
    {
        this.operation = operation;
    }

    public boolean compare(double a, double b)
    {
        return this.operation.calculate(a, b) == 1;
    }

    @SideOnly(Side.CLIENT)
    public String stringify(String a, double b)
    {
        return a + " " + this.operation.sign + " " + b;
    }

    @SideOnly(Side.CLIENT)
    public IKey stringify()
    {
        return IKey.str(this.operation.sign);
    }
}