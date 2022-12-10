package mchorse.mappet.utils;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.util.List;
import java.util.stream.Collectors;

public class ScriptUtils
{
    private static ScriptEngineManager manager;

    public static List<ScriptEngine> getAllEngines()
    {
        return getManager().getEngineFactories().stream()
            .filter(factory -> !factory.getExtensions().contains("scala"))
            .map(ScriptEngineFactory::getScriptEngine)
            .collect(Collectors.toList());
    }

    public static ScriptEngine getEngineByExtension(String extension)
    {
        extension = extension.replace(".", "");

        return getManager().getEngineByExtension(extension);
    }

    /**
     * Run something to avoid it loading first time
     */
    public static void initiateScriptEngines()
    {
        List<ScriptEngine> engineList = getAllEngines();

        for (ScriptEngine engine : engineList)
        {
            try
            {
                if (!engine.eval("true").equals(Boolean.TRUE))
                {
                    throw new Exception("Something went wrong with " + engine.getFactory().getEngineName());
                }
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