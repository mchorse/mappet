package mchorse.mappet.api.scripts;

import mchorse.mappet.api.scripts.code.ScriptEvent;
import mchorse.mappet.api.scripts.code.ScriptFactory;
import mchorse.mappet.api.utils.AbstractData;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.utils.ScriptUtils;
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
            this.engine = ScriptUtils.tryCreatingEngine();
            this.engine.getContext().setAttribute("javax.script.filename", this.getId() + ".js", ScriptContext.ENGINE_SCOPE);
            this.engine.put("mappet", new ScriptFactory());

            /* Remove */
            Bindings bindings = this.engine.getBindings(ScriptContext.ENGINE_SCOPE);

            bindings.remove("load");
            bindings.remove("loadWithNewGlobal");
            bindings.remove("exit");
            bindings.remove("quit");

            this.engine.eval(this.code);
        }
    }

    public Object execute(String function, DataContext context) throws ScriptException, NoSuchMethodException
    {
        if (function.isEmpty())
        {
            function = "main";
        }

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