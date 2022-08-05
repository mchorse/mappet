package mchorse.mappet.api.scripts;

import com.caoccao.javet.annotations.V8Function;
import com.caoccao.javet.exceptions.JavetException;
import com.caoccao.javet.interop.V8Runtime;

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
        String[] classPath = className.split("\\.");
        ImportAs(classPath[classPath.length - 1], className);
    }

    @V8Function(name = "importAs")
    public void ImportAs(String importedName, String className) throws ClassNotFoundException, JavetException {
        engine.getGlobalObject().set(importedName, Class.forName(className));
    }
}
