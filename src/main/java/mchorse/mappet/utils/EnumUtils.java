package mchorse.mappet.utils;

public class EnumUtils
{
    public static <T> T getValue(int ordinal, T[] values, T defaultValue)
    {
        if (ordinal < 0 || ordinal >= values.length)
        {
            return defaultValue;
        }

        return values[ordinal];
    }
}