package mchorse.mappet.api.utils.manager;

import mchorse.mappet.api.utils.AbstractData;
import mchorse.mappet.utils.NBTToJsonLike;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;

/**
 * Base JSON manager which loads and saves different data
 * structures based upon NBT
 */
public abstract class BaseManager <T extends AbstractData> extends FolderManager<T>
{
    public BaseManager(File folder)
    {
        super(folder);
    }

    @Override
    public final T create(String id, NBTTagCompound tag)
    {
        T data = this.createData(id, tag);

        data.setId(id);

        return data;
    }

    protected abstract T createData(String id, NBTTagCompound tag);

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

    public boolean save(String id, T data)
    {
        return this.save(id, data.serializeNBT());
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
}