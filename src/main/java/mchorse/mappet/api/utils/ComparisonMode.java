package mchorse.mappet.api.utils;

import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.math.Operation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public enum ComparisonMode
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
        public String stringify(String a, double b, String expression)
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
        public String stringify(String a, double b, String expression)
        {
            return a + " == false";
        }

        @Override
        @SideOnly(Side.CLIENT)
        public IKey stringify()
        {
            return IKey.lang("mappet.gui.conditions.comparisons.is_false");
        }
    },
    EXPRESSION(null)
    {
        @Override
        public String stringify(String a, double b, String expression)
        {
            return expression;
        }

        @Override
        public IKey stringify()
        {
            return IKey.lang("mappet.gui.conditions.expression");
        }
    },
    EQUALS_TO_STRING(null, true)
    {
        @Override
        @SideOnly(Side.CLIENT)
        public String stringify(String a, double b, String expression)
        {
            return a + " == \"" + expression + "\"";
        }

        @Override
        @SideOnly(Side.CLIENT)
        public IKey stringify()
        {
            return IKey.str("\"\"");
        }
    },
    CONTAINS_STRING(null, true)
    {
        @Override
        @SideOnly(Side.CLIENT)
        public String stringify(String a, double b, String expression)
        {
            return a + " " + IKey.lang("mappet.gui.conditions.comparisons.contains_string") + " \"" + expression + "\"";
        }

        @Override
        @SideOnly(Side.CLIENT)
        public IKey stringify()
        {
            return IKey.comp(IKey.lang("mappet.gui.conditions.comparisons.contains_string"), IKey.str(" \"\""));
        }
    },
    REGEXP_STRING(null, true)
    {
        @Override
        @SideOnly(Side.CLIENT)
        public String stringify(String a, double b, String expression)
        {
            return a + " match /" + expression + "/";
        }

        @Override
        @SideOnly(Side.CLIENT)
        public IKey stringify()
        {
            return IKey.lang("mappet.gui.conditions.comparisons.regexp");
        }
    };

    public final Operation operation;
    public final boolean isString;

    private ComparisonMode(Operation operation)
    {
        this(operation, false);
    }

    private ComparisonMode(Operation operation, boolean isString)
    {
        this.operation = operation;
        this.isString = isString;
    }

    public boolean compare(double a, double b)
    {
        return this.operation.calculate(a, b) == 1;
    }

    @SideOnly(Side.CLIENT)
    public String stringify(String a, double b, String expression)
    {
        return a + " " + this.operation.sign + " " + b;
    }

    @SideOnly(Side.CLIENT)
    public IKey stringify()
    {
        return IKey.str(this.operation.sign);
    }
}