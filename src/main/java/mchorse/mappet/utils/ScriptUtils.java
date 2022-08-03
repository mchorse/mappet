package mchorse.mappet.utils;

import com.google.common.collect.ImmutableSet;
import mchorse.mappet.CommonProxy;
import org.apache.commons.io.FileUtils;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.lang.reflect.Method;

public class ScriptUtils
{
    public static boolean copiedNashorn;
    public static boolean errorNashorn;

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
            Method getScriptEngine = factoryClass.getDeclaredMethod("getScriptEngine");

            return (ScriptEngine) getScriptEngine.invoke(factory);
        }
        catch (Exception e)
        {
            e.printStackTrace();

            tryCopyingNashorn();
        }

        errorNashorn = true;

        return null;
    }

    private static void tryCopyingNashorn()
    {
        if (copiedNashorn)
        {
            return;
        }

        File home = new File(System.getProperty("java.home"));
        File nashorn = new File(home, "lib/ext/nashorn.jar");
        File modsNashorn = new File(CommonProxy.configFolder.getParentFile(), "mods/nashorn.jar");

        if (nashorn.isFile() && !modsNashorn.isFile())
        {
            try
            {
                FileUtils.copyFile(nashorn, modsNashorn);

                copiedNashorn = true;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
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

    public static ScriptEngine sanitize(ScriptEngine engine)
    {
        /* Remove */
        Bindings bindings = engine.getBindings(ScriptContext.ENGINE_SCOPE);

        bindings.remove("load");
        bindings.remove("loadWithNewGlobal");
        bindings.remove("exit");
        bindings.remove("quit");

        return engine;
    }
}