package mchorse.mappet.api.scripts;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.exceptions.JavetTerminatedException;
import com.caoccao.javet.interception.logging.JavetStandardConsoleInterceptor;
import com.caoccao.javet.interop.IV8Executable;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.callback.JavetCallbackContext;
import com.caoccao.javet.values.V8Value;
import com.caoccao.javet.values.primitive.V8ValueString;
import com.caoccao.javet.values.reference.V8ValueGlobalObject;
import com.caoccao.javet.values.reference.V8ValueObject;
import mchorse.mappet.api.scripts.code.ScriptEvent;
import mchorse.mappet.api.scripts.code.ScriptFactory;
import mchorse.mappet.api.scripts.user.IScriptEvent;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.manager.BaseManager;
import mchorse.mappet.utils.ScriptUtils;
import mchorse.mappet.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextFormatting;
import org.apache.commons.io.FileUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ScriptManager extends BaseManager<Script>
{
    public final Map<String, Object> objects = new HashMap<String, Object>();

    private Map<String, Script> uniqueScripts = new HashMap<String, Script>();
    private Map<Object, V8Runtime> repls = new HashMap<Object, V8Runtime>();
    private String replOutput = "";

    public ScriptManager(File folder)
    {
        super(folder);
    }

    /**
     * Execute a REPL code that came from a player
     */
    public String executeRepl(Object key, String code) throws JavetException {
        V8Runtime engine = this.repls.get(key);
        ScriptEvent event;

        this.replOutput = "";

        if (engine == null)
        {
            engine = ScriptUtils.tryCreatingEngine();

            DataContext context = null;

            if (key instanceof EntityPlayerMP)
            {
                context = new DataContext((EntityPlayerMP) key);
            }
            else if (key instanceof MinecraftServer)
            {
                context = new DataContext((MinecraftServer) key);
            }

            engine.getGlobalObject().set("____manager____", this);
            engine.getGlobalObject().set("mappet", new ScriptFactory());

            if (context != null)
            {
                event = new ScriptEvent(context, "", "");

                engine.getGlobalObject().set("c", event);
            }

            this.repls.put(key, engine);
        }

        IV8Executable executable = engine.getExecutor(code).compileV8Script();
        V8Value object;

        try (ScriptGuard scriptGuard = new ScriptGuard(engine, 10000)) {
            object = executable.execute();
        }

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
    public Object execute(String id, String function, DataContext context) throws JavetException, NoSuchMethodException
    {
        Script script = getScript(id);

        return script == null ? null : script.execute(function, context);
    }

    private Script getScript(String id) throws JavetException
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
        File js = this.getJSFile(id);

        if (js != null && js.isFile())
        {
            try
            {
                String code = FileUtils.readFileToString(js, Utils.getCharset());

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
                FileUtils.writeStringToFile(this.getJSFile(id), code, Utils.getCharset());

                result = true;
            }
            catch (Exception e)
            {}
        }

        if (result)
        {
            this.uniqueScripts.remove(id);
        }

        return result;
    }

    /* Custom implementation of folder manager to support .js files */

    @Override
    public boolean exists(String name)
    {
        File js = this.getJSFile(name);

        return super.exists(name) || (js != null && js.exists());
    }

    @Override
    public boolean rename(String id, String newId)
    {
        File js = this.getJSFile(id);
        boolean result = super.rename(id, newId);

        if (js != null && js.exists())
        {
            return js.renameTo(this.getJSFile(newId)) || result;
        }

        return result;
    }

    @Override
    public boolean delete(String name)
    {
        boolean result = super.delete(name);
        File js = this.getJSFile(name);

        return (js != null && js.delete()) || result;
    }

    @Override
    protected boolean isData(File file)
    {
        return super.isData(file) || file.getName().endsWith(".js");
    }

    public File getJSFile(String id)
    {
        if (this.folder == null)
        {
            return null;
        }

        return new File(this.folder, id + ".js");
    }

    public Script getUnique(String id) throws JavetException {
        return this.getScript(id);
    }
}