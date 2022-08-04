package mchorse.mappet.events;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Runtime;
import net.minecraftforge.fml.common.eventhandler.Event;

import javax.script.ScriptEngine;

public class RegisterScriptVariablesEvent extends Event
{
    private V8Runtime engine;

    public RegisterScriptVariablesEvent(V8Runtime engine)
    {
        this.engine = engine;
    }

    public V8Runtime getEngine()
    {
        return engine;
    }

    public void register(String key, Object value) throws JavetException
    {
        this.engine.getGlobalObject().set(key, value);
    }
}