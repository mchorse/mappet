package mchorse.mappet.api.conditions.utils;

import mchorse.mclib.math.Operation;

public enum Comparison
{
    LESS(Operation.LESS), LESS_THAN(Operation.LESS_THAN), EQUALS(Operation.EQUALS), GREATER_THAN(Operation.GREATER_THAN), GREATER(Operation.GREATER);

    public final Operation operation;

    private Comparison(Operation operation)
    {
        this.operation = operation;
    }

    public boolean compare(double a, double b)
    {
        return this.operation.calculate(a, b) == 1;
    }
}