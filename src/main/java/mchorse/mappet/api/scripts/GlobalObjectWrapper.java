package mchorse.mappet.api.scripts;

import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.values.V8Value;

public class GlobalObjectWrapper {
    protected V8Runtime engine;

    public GlobalObjectWrapper(V8Runtime engine) {
        this.engine = engine;
    }

    public Object get(String name) throws JavetException {
        return engine.getGlobalObject().get(name);
    }

    public void set(String name, Object value) throws JavetException {
        engine.getGlobalObject().set(name, value);
    }
}