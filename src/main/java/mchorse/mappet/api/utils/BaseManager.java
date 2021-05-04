package mchorse.mappet.api.utils;

import mchorse.mappet.utils.NBTToJsonLike;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Base JSON manager which loads and saves different data
 * structures based upon NBT
 */
public abstract class BaseManager <T extends AbstractData> implements IManager<T>
{
    private File folder;

    public BaseManager(File folder)
    {
        if (folder != null)
        {
            this.folder = folder;
            this.folder.mkdirs();
        }
    }

    @Override
    public final T create(String id, NBTTagCompound tag)
    {
        T data = this.createData(tag);

        data.setId(id);

        return data;
    }

    protected abstract T createData(NBTTagCompound tag);

    @Override
    public T load(String id)
    {
        try
        {
            File file = this.getFile(id);
            String json = FileUtils.readFileToString(file, Charset.defaultCharset());
            NBTTagCompound tag = NBTToJsonLike.fromJson(json);
            T data = this.create(id);

            data.deserializeNBT(tag);

            return data;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public boolean save(String name, T data)
    {
        return this.save(name, data.serializeNBT());
    }

    @Override
    public boolean save(String name, NBTTagCompound tag)
    {
        try
        {
            File file = this.getFile(name);
            String element = NBTToJsonLike.toJson(tag);

            FileUtils.writeStringToFile(file, element, Charset.defaultCharset());

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean rename(String id, String newId)
    {
        File file = this.getFile(id);

        if (file != null && file.exists())
        {
            return file.renameTo(this.getFile(newId));
        }

        return false;
    }

    @Override
    public boolean delete(String name)
    {
        File file = this.getFile(name);

        return file != null && file.delete();
    }

    @Override
    public Collection<String> getKeys()
    {
        List<String> list = new ArrayList<String>();

        if (this.folder == null)
        {
            return list;
        }

        for (File file : this.folder.listFiles())
        {
            String name = file.getName();

            if (file.isFile() && name.endsWith(".json"))
            {
                list.add(name.substring(0, name.lastIndexOf(".")));
            }
        }

        return list;
    }

    public File getFile(String name)
    {
        if (this.folder == null)
        {
            return null;
        }

        return new File(this.folder, name + ".json");
    }
}