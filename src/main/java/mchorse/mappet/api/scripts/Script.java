package mchorse.mappet.api.scripts;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.scripts.code.ScriptEvent;
import mchorse.mappet.api.scripts.code.ScriptFactory;
import mchorse.mappet.api.utils.AbstractData;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.events.RegisterScriptVariablesEvent;
import mchorse.mappet.utils.ScriptUtils;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;

import javax.script.Bindings;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class Script extends AbstractData
{
    public String code = "";
    public boolean unique;

    private ScriptEngine engine;

    public Script()
    {}

    public void start() throws ScriptException
    {
        if (this.engine == null)
        {
            this.engine = ScriptUtils.sanitize(ScriptUtils.tryCreatingEngine());
            this.engine.getContext().setAttribute("javax.script.filename", this.getId() + ".js", ScriptContext.ENGINE_SCOPE);

            Mappet.EVENT_BUS.post(new RegisterScriptVariablesEvent(this.engine));

            this.engine.put("mappet", new ScriptFactory());
            this.engine.eval(this.code);
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

        tag.setBoolean("Unique", this.unique);
        tag.setByteArray("Code", this.code.getBytes());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.unique = tag.getBoolean("Unique");
        this.code = new String(tag.getByteArray("Code"));
    }
}