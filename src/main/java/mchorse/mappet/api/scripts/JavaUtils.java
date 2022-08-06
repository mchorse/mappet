package mchorse.mappet.api.scripts;

import com.caoccao.javet.annotations.V8Function;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Runtime;
import com.caoccao.javet.interop.converters.IJavetConverter;
import com.caoccao.javet.values.V8Value;

public class JavaUtils {
    private V8Runtime engine;

    public JavaUtils(V8Runtime engine) {
        this.engine = engine;
    }

    @SuppressWarnings("unused")
    @V8Function(name = "type")
    public Class<?> GetType(String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            return null;
        }
    }

    @V8Function(name = "import")
    public void Import(String className) throws ClassNotFoundException, JavetException {
        engine.getGlobalObject().set(className.substring(className.lastIndexOf('.')), Class.forName(className));
    }

    @V8Function(name = "importAs")
    public void ImportAs(String importedName, String className) throws ClassNotFoundException, JavetException {
        engine.getGlobalObject().set(importedName, Class.forName(className));
    }

    @V8Function(name = "from")
    public V8Value From(Object obj) throws JavetException {
        IJavetConverter objectConverter = engine.getConverter();
        objectConverter.getConfig().setProxyMapEnabled(false);
        objectConverter.getConfig().setProxySetEnabled(false);

        V8Value valueObject = objectConverter.toV8Value(engine, obj);

        objectConverter.getConfig().setProxySetEnabled(true);
        objectConverter.getConfig().setProxyMapEnabled(true);
        return valueObject;
    }
    @V8Function(name = "to")
    public Object To(V8Value value) throws JavetException {
        return engine.getConverter().toObject(value);
    }
}
