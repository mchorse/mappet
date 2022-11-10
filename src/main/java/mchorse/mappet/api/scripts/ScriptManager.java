package mchorse.mappet.api.scripts;

import mchorse.mappet.api.scripts.code.ScriptEvent;
import mchorse.mappet.api.scripts.code.ScriptFactory;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.manager.BaseManager;
import mchorse.mappet.utils.ScriptUtils;
import mchorse.mappet.utils.Utils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.io.FileUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ScriptManager extends BaseManager<Script>
{
    public final Map<String, Object> objects = new HashMap<String, Object>();

    private Map<String, Script> uniqueScripts = new HashMap<String, Script>();
    private Map<Object, ScriptEngine> repls = new HashMap<Object, ScriptEngine>();
    private String replOutput = "";

    public ScriptManager(File folder)
    {
        super(folder);
        ScriptUtils.getAllEngines();
    }

    /**
     * Execute a REPL code that came from a player
     */
    public String executeRepl(Object key, String code) throws ScriptException
    {
        ScriptEngine engine = this.repls.get(key);

        this.replOutput = "";

        if (engine == null)
        {
            engine = ScriptUtils.sanitize(ScriptUtils.getEngineByExtension(".js"));

            DataContext context = null;

            if (key instanceof EntityPlayerMP)
            {
                context = new DataContext((EntityPlayerMP) key);
            }
            else if (key instanceof MinecraftServer)
            {
                context = new DataContext((MinecraftServer) key);
            }

            engine.put("____manager____", this);
            engine.put("mappet", new ScriptFactory());

            if (context != null)
            {
                ScriptEvent event = new ScriptEvent(context, "", "");

                engine.put("c", event);
                engine.put("s", event.getSubject());
            }

            engine.eval("var __p__ = print; print = function(message) { ____manager____.replPrint(message); __p__(message); };");

            this.repls.put(key, engine);
        }

        Object object = engine.eval(code);

        if (this.replOutput.isEmpty())
        {
            this.replPrint(object);
        }

        return this.replOutput;
    }

    public void replPrint(Object object)
    {
        if (object == null)
        {
            object = TextFormatting.GRAY + "undefined";
        }

        this.replOutput += object.toString() + "\n";
    }

    /**
     * Execute given script
     */
    public Object execute(String id, String function, DataContext context) throws ScriptException, NoSuchMethodException
    {
        Script script = getScript(id);

        return script == null ? null : script.execute(function, context);
    }

    private Script getScript(String id) throws ScriptException
    {
        Script script = this.uniqueScripts.get(id);

        if (script == null)
        {
            script = this.load(id);

            if (script != null && script.unique)
            {
                this.uniqueScripts.put(id, script);
            }
        }

        if (script == null)
        {
            return null;
        }

        script.start(this);

        return script;
    }

    @Override
    public Collection<String> getKeys()
    {
        Set<String> set = new HashSet<String>();

        if (this.folder == null)
        {
            return set;
        }

        for (File file : this.folder.listFiles())
        {
            String name = file.getName();

            if (file.isFile() && this.isData(file))
            {
                set.add(name.replace(".json", ""));
            }
        }

        return set;
    }

    @Override
    protected Script createData(String id, NBTTagCompound tag)
    {
        Script script = new Script();

        if (tag != null)
        {
            script.deserializeNBT(tag);
        }

        return script;
    }

    /* Custom implementation of base manager to support .js files */

    @Override
    public Script load(String id)
    {
        Script script = super.load(id);
        File scriptFile = this.getScriptFile(id);

        if (scriptFile != null && scriptFile.isFile())
        {
            try
            {
                String code = FileUtils.readFileToString(scriptFile, Utils.getCharset());

                if (script == null)
                {
                    script = new Script();
                }

                script.code = code.replaceAll("\t", "    ").replaceAll("\r", "");
            }
            catch (Exception e)
            {}
        }

        return script;
    }

    @Override
    public boolean save(String id, NBTTagCompound tag)
    {
        String code = new String(tag.getByteArray("Code"));

        tag.removeTag("Code");

        boolean result = super.save(id, tag);

        if (!code.trim().isEmpty())
        {
            try
            {
                FileUtils.writeStringToFile(this.getScriptFile(id), code, Utils.getCharset());

                result = true;
            }
            catch (Exception e)
            {}
        }

        if (result)
        {
            this.uniqueScripts.remove(id);

            Script script = this.load(id);

            if (script != null && script.unique)
            {
                this.uniqueScripts.put(id, script);
            }
        }

        return result;
    }

    /* Custom implementation of folder manager to support .js files */

    @Override
    public boolean exists(String name)
    {
        File scriptFile = this.getScriptFile(name);

        return super.exists(name) || (scriptFile != null && scriptFile.exists());
    }

    @Override
    public boolean rename(String id, String newId)
    {
        File scriptFile = this.getScriptFile(id);
        boolean result = super.rename(id, newId);

        if (scriptFile != null && scriptFile.exists())
        {
            return scriptFile.renameTo(this.getScriptFile(newId)) || result;
        }

        return result;
    }

    @Override
    public boolean delete(String name)
    {
        boolean result = super.delete(name);
        File scriptFile = this.getScriptFile(name);

        return (scriptFile != null && scriptFile.delete()) || result;
    }

    @Override
    protected boolean isData(File file)
    {
        return super.isData(file) || !file.getName().endsWith(".json");
    }

    public File getScriptFile(String id)
    {
        if (this.folder == null)
        {
            return null;
        }


        return new File(this.folder, id);
    }

    public void initiateAllScripts()
    {
        Collection<String> scriptIds = getKeys();

        for (String id : scriptIds)
        {
            try
            {
                Script script = this.load(id);

                if (script != null && script.unique)
                {
                    this.uniqueScripts.put(id, script);
                    script.start(this);
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}