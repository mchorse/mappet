package mchorse.mappet.utils;

import com.caoccao.javet.interop.V8Host;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.converters.JavetProxyConverter;

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
            engine.setConverter(new JavetProxyConverter());

            engine.getGlobalObject().set("Java", new Object() {
                @SuppressWarnings("unused")
                public Class<?> type(String className) throws ClassNotFoundException {
                    return Class.forName(className);
                }
            });
            engine.getGlobalObject().set("import", new Runnable() {
                @Override
                public void run() {

                }
            });

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
