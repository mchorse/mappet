package mchorse.mappet.api.quests;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import mchorse.mappet.api.utils.IManager;
import mchorse.mappet.utils.NBTToJson;
import mchorse.mclib.utils.JsonUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class QuestManager implements IManager<Quest>
{
    /**
     * Quest folder where all the quests will be stored
     */
    private File folder;

    /**
     * Quest cache
     */
    private Map<String, Quest> cache = new HashMap<String, Quest>();

    public QuestManager(File folder)
    {
        this.folder = folder;
        this.folder.mkdirs();
    }

    public void loadCache()
    {
        File file = new File(this.folder, "main.json");

        try
        {
            JsonElement element = new JsonParser().parse(FileUtils.readFileToString(file, Charset.defaultCharset()));
            NBTTagCompound tag = (NBTTagCompound) NBTToJson.fromJson(element);

            for (String key : tag.getKeySet())
            {
                Quest quest = new Quest();

                quest.deserializeNBT(tag.getCompoundTag(key));
                this.cache.put(quest.getId(), quest);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void add(Quest quest)
    {
        this.cache.put(quest.getId(), quest);
    }

    @Override
    public Quest create(NBTTagCompound tag)
    {
        Quest quest = new Quest();

        if (tag != null)
        {
            quest.deserializeNBT(tag);
        }

        return quest;
    }

    @Override
    public boolean rename(String id, String newId)
    {
        boolean exists = this.cache.containsKey(id);

        if (exists)
        {
            Quest quest = this.cache.remove(id);

            quest.setId(newId);
            this.cache.put(newId, quest);
        }

        return exists;
    }

    /**
     * Load a quest
     * @param id
     */
    @Override
    public Quest load(String id)
    {
        Quest cached = this.cache.get(id);

        if (cached != null)
        {
            return cached.copy();
        }

        return null;
    }

    /**
     * Save quest to server's directory
     */
    public void save()
    {
        NBTTagCompound tag = new NBTTagCompound();

        for (Map.Entry<String, Quest> entry : this.cache.entrySet())
        {
            tag.setTag(entry.getKey(), entry.getValue().serializeNBT());
        }

        try
        {
            String string = JsonUtils.jsonToPretty(NBTToJson.toJson(tag));

            FileUtils.writeStringToFile(new File(this.folder, "main.json"), string, Charset.defaultCharset());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public boolean save(String name, NBTTagCompound tag)
    {
        Quest quest = new Quest();

        quest.deserializeNBT(tag);
        this.cache.put(name, quest);

        return true;
    }

    /**
     * Delete quest from server's directory
     */
    @Override
    public boolean delete(String id)
    {
        return this.cache.remove(id) != null;
    }

    @Override
    public Collection<String> getKeys()
    {
        return this.cache.keySet();
    }
}