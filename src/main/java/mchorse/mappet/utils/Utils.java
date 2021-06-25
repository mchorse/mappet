package mchorse.mappet.utils;

import mchorse.mappet.Mappet;

import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public class Utils
{
    public static Charset getCharset()
    {
        try
        {
            if (Mappet.generalEncoding != null)
            {
                return Charset.forName(Mappet.generalEncoding.get());
            }
        }
        catch (UnsupportedCharsetException e)
        {}

        return Charset.defaultCharset();
    }
}