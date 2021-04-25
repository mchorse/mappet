package mchorse.mappet.api.states;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import mchorse.mclib.utils.JsonUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * States allow to store values of the world that can be
 * used in dialogues, crafting tables, events and etc.
 * to control logic and store arbitrary numerical values
 */
public class States implements INBTSerializable<NBTTagCompound>
{
    public Map<String, Double> values = new HashMap<String, Double>();

    private File file;

    public States()
    {}

    public States(File file)
    {
        this.file = file;
    }

    /* CRUD */

    public void add(String id, double value)
    {
        Double previous = this.values.get(id);

        this.values.put(id, (previous == null ? 0 : previous) + value);
    }

    public void set(String id, double value)
    {
        this.values.put(id, value);
    }

    public double get(String id)
    {
        return this.values.containsKey(id) ? this.values.get(id) : 0;
    }

    public boolean reset(String id)
    {
        boolean existed = this.values.containsKey(id);

        this.values.remove(id);

        return !existed;
    }

    /* Quest convenience methods */

    public void completeQuest(String id)
    {
        this.add("quests." + id, 1);
    }

    public boolean wasQuestCompleted(String id)
    {
        return this.get("quests." + id) > 0;
    }

    /* Faction convenience methods */

    public void addFactionScore(String id, int score)
    {
        this.add("factions." + id, score);
    }

    public void setFactionScore(String id, int score)
    {
        this.set("factions." + id, score);
    }

    public int getFactionScore(String id)
    {
        return (int) this.get("factions." + id);
    }

    public boolean clearFactionScore(String id)
    {
        return this.reset("factions." + id);
    }

    public void clearAllFactionScores()
    {
        this.values.keySet().removeIf((key) -> key.startsWith("factions."));
    }

    public boolean hasFaction(String id)
    {
        return this.values.containsKey("factions." + id);
    }

    /* NBT */

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        for (Map.Entry<String, Double> entry : this.values.entrySet())
        {
            tag.setDouble(entry.getKey(), entry.getValue());
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.values.clear();

        for (String key : tag.getKeySet())
        {
            this.values.put(key, tag.getDouble(key));
        }
    }

    /* Deserialization and serialization */

    public void load()
    {
        try
        {
            String json = FileUtils.readFileToString(this.file, Charset.defaultCharset());

            this.values = new Gson().fromJson(json, new TypeToken<Map<String, Double>>(){}.getType());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public boolean save()
    {
        try
        {
            String json = JsonUtils.jsonToPretty(new Gson().toJsonTree(this.values));

            FileUtils.writeStringToFile(this.file, json, Charset.defaultCharset());

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}