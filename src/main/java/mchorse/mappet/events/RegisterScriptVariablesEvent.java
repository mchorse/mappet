package mchorse.mappet.events;

import net.minecraftforge.fml.common.eventhandler.Event;

import javax.script.ScriptEngine;

public class RegisterScriptVariablesEvent extends Event
{
    private ScriptEngine engine;

    public RegisterScriptVariablesEvent(ScriptEngine engine)
    {
        this.engine = engine;
    }

    public ScriptEngine getEngine()
    {
        return engine;
    }

    public void register(String key, Object value)
    {
        this.engine.put(key, value);
    }
}