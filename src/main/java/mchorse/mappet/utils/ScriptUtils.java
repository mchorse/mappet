package mchorse.mappet.utils;

import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.converters.JavetProxyConverter;
import mchorse.mappet.api.scripts.JavaUtils;

public class ScriptUtils
{
    private static V8Host v8Host;

    /**
     * Tries to create a script engine
     */
    public static V8Runtime tryCreatingEngine()
    {
        try {
            V8Runtime engine = getManager().createV8Runtime();
            JavetProxyConverter javetProxyConverter = new JavetProxyConverter();
            javetProxyConverter.getConfig().setProxyMapEnabled(true);
            javetProxyConverter.getConfig().setProxySetEnabled(true);
            engine.setConverter(javetProxyConverter);

            engine.getGlobalObject().set("Java", new JavaUtils(engine));

            return engine;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static V8Host getManager()
    {
        try
        {
            if (v8Host == null)
            {
                v8Host = V8Host.getV8Instance();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return v8Host;
    }
}
