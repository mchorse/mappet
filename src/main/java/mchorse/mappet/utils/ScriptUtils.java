package mchorse.mappet.utils;

import jdk.nashorn.api.scripting.ScriptObjectMirror;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ScriptUtils
{
    private static ScriptEngineManager manager;

    public static List<ScriptEngine> getAllEngines()
    {
        return getManager().getEngineFactories().stream()
                .filter(factory -> !factory.getExtensions().contains("scala"))
                .map(factory ->
                {
                    try
                    {
                        return factory.getScriptEngine();
                    }
                    catch (Exception | NoClassDefFoundError ignored)
                    {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public static ScriptEngine getEngineByExtension(String extension)
    {
        extension = extension.replace(".", "");

        ScriptEngine engine = getManager().getEngineByExtension(extension);

        if (extension.equals("py"))
        {
            try
            {
                Field fieldInterpreter = Class.forName("org.python.jsr223.PyScriptEngine").getDeclaredField("interp");
                fieldInterpreter.setAccessible(true);
                Object interpreter = fieldInterpreter.get(engine);

                Field fieldcFlags = Class.forName("org.python.util.PythonInterpreter").getDeclaredField("cflags");
                fieldcFlags.setAccessible(true);
                Object cFlags = fieldcFlags.get(interpreter);

                Class.forName("org.python.core.CompilerFlags").getDeclaredField("source_is_utf8").setBoolean(cFlags, true);

                return engine;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }

        return engine;
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
                if (!engine.eval(Objects.equals(engine.getFactory().getLanguageName(), "python") ? "True" : "true").equals(Boolean.TRUE))
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

    public static String getScriptContent(ScriptObjectMirror script) {
        String fullScript = script.toString();
        String scriptContent = "";

        Pattern pattern = Pattern.compile("\\{(.*)\\}", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(fullScript);
        if (matcher.find()) {
            scriptContent = matcher.group(1).trim();
        }

        /* Formatting the script content */

        // Splitting the script content into lines
        String[] lines = scriptContent.split("\\n");

        // Get the common spaces
        int commonSpaces = Integer.MAX_VALUE;
        for (int i = 1; i < lines.length; i++) {
            int spaceCount = 0;
            while (lines[i].length() > spaceCount && lines[i].charAt(spaceCount) == ' ') {
                spaceCount++;
            }
            commonSpaces = Math.min(commonSpaces, spaceCount);
        }

        // Removing the common leading spaces
        StringBuilder adjustedScript = new StringBuilder(lines[0].trim() + "\n");
        for (int i = 1; i < lines.length; i++) {
            adjustedScript.append(lines[i].substring(commonSpaces)).append("\n");
        }
        scriptContent = adjustedScript.toString().trim(); // Removing trailing new line

        return scriptContent;
    }
}