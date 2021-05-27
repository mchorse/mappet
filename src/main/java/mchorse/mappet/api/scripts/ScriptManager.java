package mchorse.mappet.api.scripts;

import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.manager.FolderManager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.File;
import java.io.FileReader;

public class ScriptManager extends FolderManager
{
    private ScriptEngineManager manager;

    public ScriptManager(File folder)
    {
        super(folder);

        try
        {
            this.manager = new ScriptEngineManager();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean execute(String script, DataContext context)
    {
        File file = getFile(script);

        try
        {
            ScriptEngine engine = this.manager.getEngineByName("nashorn");

            engine.eval(new FileReader(file));

            ((Invocable) engine).invokeFunction("main", context);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    protected String getExtension()
    {
        return ".js";
    }

    /* Unused */

    @Override
    public INBTSerializable<NBTTagCompound> create(String id, NBTTagCompound tag)
    {
        throw new UnsupportedOperationException("Method create(String, NBTTagCompound) isn't supported!");
    }

    @Override
    public INBTSerializable<NBTTagCompound> load(String id)
    {
        throw new UnsupportedOperationException("Method load(String) isn't supported!");
    }

    @Override
    public boolean save(String name, NBTTagCompound tag)
    {
        throw new UnsupportedOperationException("Method save(String, NBTTagCompound) isn't supported!");
    }
}