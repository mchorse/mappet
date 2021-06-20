package mchorse.mappet.utils;

import com.google.common.collect.ImmutableSet;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.lang.reflect.Method;

public class ScriptUtils
{
    private static ScriptEngineManager manager;

    /**
     * Tries to create a script engine
     */
    public static ScriptEngine tryCreatingEngine()
    {
        for (String name : ImmutableSet.of("nashorn", "Nashorn", "javascript", "JavaScript", "js", "JS", "ecmascript", "ECMAScript"))
        {
            ScriptEngine engine = getManager().getEngineByName(name);

            if (engine != null)
            {
                return engine;
            }
        }

        try
        {
            Class factoryClass = Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory");
            Object factory = factoryClass.getConstructor().newInstance();
            Method getScriptEnging = factoryClass.getDeclaredMethod("getScriptEngine");

            return (ScriptEngine) getScriptEnging.invoke(factory);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public static ScriptEngineManager getManager()
    {
        try
        {
            if (manager == null)
            {
                manager = new ScriptEngineManager();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return manager;
    }
}