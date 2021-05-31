package mchorse.mappet.api.scripts;

import mchorse.mappet.api.scripts.code.ScriptEvent;
import mchorse.mappet.api.scripts.code.ScriptFactory;
import mchorse.mappet.api.utils.AbstractData;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.nbt.NBTTagCompound;

import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Script extends AbstractData
{
    public String code = "";
    public boolean unique;

    private ScriptEngine engine;

    public Script()
    {}

    public void start(ScriptEngineManager manager) throws ScriptException
    {
        if (this.engine == null)
        {
            this.engine = manager.getEngineByName("nashorn");
            this.engine.getContext().setAttribute("javax.script.filename", this.getId() + ".js", ScriptContext.ENGINE_SCOPE);
            this.engine.eval(this.code);
            this.engine.put("mappet", new ScriptFactory());
        }
    }

    public Object execute(String function, DataContext context) throws ScriptException, NoSuchMethodException
    {
        return ((Invocable) this.engine).invokeFunction(function, new ScriptEvent(context));
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setBoolean("Unique", this.unique);
        tag.setString("Code", this.code);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.unique = tag.getBoolean("Unique");
        this.code = tag.getString("Code");
    }
}