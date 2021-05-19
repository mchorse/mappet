package mchorse.mappet.utils;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mclib.math.IValue;
import mchorse.mclib.utils.Rewriter;

import java.text.DecimalFormat;

public class ExpressionRewriter extends Rewriter
{
    public DataContext context;
    public DecimalFormat formater;

    public ExpressionRewriter()
    {
        super("\\$\\{([^\\}]+)\\}");

        this.formater = new DecimalFormat("0.######");
    }

    public ExpressionRewriter set(DataContext context)
    {
        this.context = context;

        return this;
    }

    @Override
    public String replacement()
    {
        String string = this.group(1);
        Object value = this.context.getValue(string);

        if (value == null)
        {
            IValue v = Mappet.expressions.set(this.context).parse(string, null);

            if (v != null)
            {
                if (v.isNumber())
                {
                    value = v.doubleValue();
                }
                else
                {
                    value = v.stringValue();
                }
            }
        }

        if (value instanceof Number)
        {
            return this.formater.format(((Number) value).doubleValue());
        }
        else if (value instanceof String)
        {
            return (String) value;
        }

        return "";
    }
}