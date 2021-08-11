package mchorse.mappet.api.scripts;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.scripts.code.ScriptEvent;
import mchorse.mappet.api.scripts.code.ScriptFactory;
import mchorse.mappet.api.utils.AbstractData;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.events.RegisterScriptVariablesEvent;
import mchorse.mappet.utils.ScriptUtils;
import mchorse.mappet.utils.Utils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;
import org.apache.commons.io.FileUtils;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Script extends AbstractData
{
    public String code = "";
    public boolean unique;
    public List<String> libraries = new ArrayList<String>();

    private ScriptEngine engine;

    public Script()
    {}

    public void start(ScriptManager manager) throws ScriptException
    {
        if (this.engine == null)
        {
            this.engine = ScriptUtils.sanitize(ScriptUtils.tryCreatingEngine());
            this.engine.getContext().setAttribute("javax.script.filename", this.getId() + ".js", ScriptContext.ENGINE_SCOPE);

            Mappet.EVENT_BUS.post(new RegisterScriptVariablesEvent(this.engine));

            StringBuilder finalCode = new StringBuilder();
            Set<String> alreadyLoaded = new HashSet<String>();

            for (String library : this.libraries)
            {
                /* Don't load this script as its own library nor load repeatedly
                 * same library twice or more */
                if (library.equals(this.getId()) || alreadyLoaded.contains(library))
                {
                    continue;
                }

                try
                {
                    File jsFile = manager.getJSFile(library);

                    finalCode.append(FileUtils.readFileToString(jsFile, Utils.getCharset()));
                }
                catch (Exception e)
                {
                    System.err.println("[Mappet] Script library " + library + ".js failed to load...");
                    e.printStackTrace();
                }

                alreadyLoaded.add(library);
            }

            finalCode.append(this.code);

            this.engine.put("mappet", new ScriptFactory());
            this.engine.eval(finalCode.toString());
        }
    }

    public Object execute(String function, DataContext context) throws ScriptException, NoSuchMethodException
    {
        if (function.isEmpty())
        {
            function = "main";
        }

        return ((Invocable) this.engine).invokeFunction(function, new ScriptEvent(context, this.getId(), function));
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();
        NBTTagList libraries = new NBTTagList();

        for (String library : this.libraries)
        {
            libraries.appendTag(new NBTTagString(library));
        }

        tag.setBoolean("Unique", this.unique);
        tag.setTag("Libraries", libraries);
        tag.setByteArray("Code", this.code.getBytes());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.unique = tag.getBoolean("Unique");

        if (tag.hasKey("Libraries", Constants.NBT.TAG_LIST))
        {
            NBTTagList libraries = tag.getTagList("Libraries", Constants.NBT.TAG_STRING);

            this.libraries.clear();

            for (int i = 0, c = libraries.tagCount(); i < c; i++)
            {
                this.libraries.add(libraries.getStringTagAt(i));
            }
        }

        this.code = new String(tag.getByteArray("Code"));
    }
}