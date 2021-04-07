package mchorse.mappet.api.quests;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import mchorse.mappet.utils.NBTToJson;
import mchorse.mclib.utils.JsonUtils;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QuestManager
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

    /**
     * Reset cache
     */
    public void resetCache()
    {
        this.cache.clear();
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

    /**
     * Load a quest
     */
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

    /**
     * Delete quest from server's directory
     */
    public boolean delete(String id)
    {
        return this.cache.remove(id) != null;
    }

    public Set<String> getKeys()
    {
        return this.cache.keySet();
    }
}