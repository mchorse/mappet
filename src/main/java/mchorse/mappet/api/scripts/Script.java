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
import org.apache.commons.lang3.StringUtils;

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
    public boolean unique = true;
    public List<String> libraries = new ArrayList<String>();

    private ScriptEngine engine;
    private List<ScriptRange> ranges;

    public Script()
    {}

    public void start(ScriptManager manager) throws ScriptException
    {
        if (this.engine == null)
        {
            this.engine = ScriptUtils.getEngineByExtension(this.getScriptExtension());

            if (this.engine == null)
            {
                String message = "Looks like Mappet can't find script engine for a \"" + this.getScriptExtension() + "\" file extension.";

                throw new ScriptException(message, this.getId(), -1);
            }

            this.engine = ScriptUtils.sanitize(this.engine);
            this.engine.getContext().setAttribute("javax.script.filename", this.getId(), ScriptContext.ENGINE_SCOPE);
            Mappet.EVENT_BUS.post(new RegisterScriptVariablesEvent(this.engine));

            StringBuilder finalCode = new StringBuilder();
            Set<String> alreadyLoaded = new HashSet<String>();
            int total = 0;

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
                    File scriptFile = manager.getScriptFile(library);
                    String code = FileUtils.readFileToString(scriptFile, Utils.getCharset());

                    finalCode.append(code);
                    finalCode.append("\n");

                    if (this.ranges == null)
                    {
                        this.ranges = new ArrayList<ScriptRange>();
                    }

                    this.ranges.add(new ScriptRange(total, library));

                    total += StringUtils.countMatches(code, "\n") + 1;
                }
                catch (Exception e)
                {
                    System.err.println("[Mappet] Script library " + library + ".js failed to load...");
                    e.printStackTrace();
                }

                alreadyLoaded.add(library);
            }

            finalCode.append(this.code);

            if (this.ranges != null)
            {
                this.ranges.add(new ScriptRange(total, this.getId()));
            }

            this.engine.put("mappet", new ScriptFactory());
            this.engine.eval(finalCode.toString());
        }
    }

    public String getScriptExtension()
    {
        String id = this.getId();
        int index = id.lastIndexOf('.');

        return index >= 0 ? id.substring(index) : "js";
    }

    public Object execute(String function, DataContext context) throws ScriptException, NoSuchMethodException
    {
        if (function.isEmpty())
        {
            function = "main";
        }

        try
        {
            return ((Invocable) this.engine).invokeFunction(function, new ScriptEvent(context, this.getId(), function));
        }
        catch (ScriptException e)
        {
            ScriptException exception = processScriptException(e);

            throw exception == null ? e : exception;
        }
    }

    private ScriptException processScriptException(ScriptException e)
    {
        if (this.ranges == null)
        {
            return null;
        }

        ScriptRange range = null;

        for (int i = this.ranges.size() - 1; i >= 0; i--)
        {
            ScriptRange possibleRange = this.ranges.get(i);

            if (possibleRange.lineOffset <= e.getLineNumber() - 1)
            {
                range = possibleRange;

                break;
            }
        }

        if (range != null)
        {
            String message = e.getMessage();
            int lineNumber = e.getLineNumber() - range.lineOffset;
            message = message.replaceFirst(this.getId(), range.script + " (in " + this.getId() + ")");
            message = message.replaceFirst("at line number [\\d]+", "at line number " + lineNumber);

            return new ScriptException(message, range.script, lineNumber, e.getColumnNumber());
        }

        return null;
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