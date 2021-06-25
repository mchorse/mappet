package mchorse.mappet.api.scripts;

import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.manager.BaseManager;
import mchorse.mappet.utils.Utils;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.io.FileUtils;

import javax.script.ScriptException;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ScriptManager extends BaseManager<Script>
{
    public final Map<String, Object> objects = new HashMap<String, Object>();

    private Map<String, Script> uniqueScripts = new HashMap<String, Script>();

    public ScriptManager(File folder)
    {
        super(folder);
    }

    /**
     * Execute given script
     */
    public Object execute(String id, String function, DataContext context) throws ScriptException, NoSuchMethodException
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

        script.start();

        return script.execute(function, context);
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
        String code = tag.getString("Code");

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
}