package mchorse.mappet.utils;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.scripts.Script;
import net.minecraft.launchwrapper.Launch;
import org.apache.commons.io.FileUtils;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class ScriptUtils
{
    public static boolean copiedNashorn;
    public static boolean errorScriptEngine;

    private static List<ScriptEngine> engines;
    private static ScriptEngineManager manager;

    public static List<ScriptEngine> getAllEngines()
    {
        if(engines == null)
        {
            engines = getManager().getEngineFactories().stream()
                    .filter(factory -> !factory.getExtensions().contains("scala"))
                    .map(ScriptEngineFactory::getScriptEngine)
                    .collect(Collectors.toList());
        }

        return engines;
    }

    public static ScriptEngine getEngineByExtension(String extension)
    {
        List<ScriptEngine> engines = ScriptUtils.getAllEngines();

        for (ScriptEngine engine : engines)
        {
            List<String> extensions = engine.getFactory().getExtensions();

            if (extensions.contains(extension.replace(".","")))
            {
                return engine;
            }
        }

        return null;
    }

    /**
     * Run something to avoid it loading first time
     */
    public static void initiateScriptEngines()
    {
        List<ScriptEngine> engineList = ScriptUtils.getAllEngines();
        for (ScriptEngine engine : engineList)
        {
            try
            {
                if (!engine.eval("true").equals(Boolean.TRUE))
                {
                    throw new Exception("Something went wrong with "+engine.getFactory().getEngineName());
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
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