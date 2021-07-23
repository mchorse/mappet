package mchorse.mappet.api.utils.manager;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.utils.AbstractData;
import mchorse.mappet.utils.NBTToJsonLike;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

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
            NBTTagCompound tag = this.getCached(id);
            T data = this.create(id, tag);

            return data;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Get cached NBT tag compound from given file by ID
     */
    protected NBTTagCompound getCached(String id) throws Exception
    {
        NBTTagCompound tag = null;
        File file = this.getFile(id);
        boolean isCaching = Mappet.generalDataCaching.get();
        long lastUpdated = file.lastModified();

        if (isCaching)
        {
            ManagerCache cache = this.cache.get(id);

            if (cache != null)
            {
                /* This is necessary for update if the files were edited externally,
                 * because dashboard save will clear the cache for sure */
                if (cache.lastUpdated < lastUpdated)
                {
                    this.cache.remove(id);
                }
                else
                {
                    tag = cache.tag;

                    cache.update();
                }

                this.doExpirationCheck();
            }
        }

        if (tag == null)
        {
            tag = NBTToJsonLike.read(file);

            if (isCaching)
            {
                this.cache.put(id, new ManagerCache(tag, lastUpdated));
            }
        }

        return tag;
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
            NBTToJsonLike.write(this.getFile(name), tag);
            this.cache.remove(name);

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}