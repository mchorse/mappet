package mchorse.mappet.api.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import mchorse.mappet.utils.NBTToJson;
import mchorse.mclib.utils.JsonUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
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
public abstract class BaseManager <T extends INBTSerializable<NBTTagCompound>> implements IManager<T>
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
    public T load(String id)
    {
        try
        {
            File file = this.getFile(id);
            String json = FileUtils.readFileToString(file, Charset.defaultCharset());
            JsonElement element = new JsonParser().parse(json);
            NBTTagCompound tag = (NBTTagCompound) NBTToJson.fromJson(element);
            T data = this.create();

            data.deserializeNBT(tag);

            if (data instanceof IID)
            {
                ((IID) data).setId(id);
            }

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
            JsonElement element = NBTToJson.toJson(tag);

            FileUtils.writeStringToFile(file, JsonUtils.jsonToPretty(element), Charset.defaultCharset());

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
