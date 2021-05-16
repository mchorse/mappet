package mchorse.mappet.api.states;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import mchorse.mappet.Mappet;
import mchorse.mappet.events.StateChangedEvent;
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
    public static final String QUEST_PREFIX = "quests.";
    public static final String DIALOGUE_PREFIX = "dialogue.";
    public static final String FACTIONS_PREFIX = "factions.";

    public Map<String, Double> values = new HashMap<String, Double>();

    private File file;

    public States()
    {}

    public States(File file)
    {
        this.file = file;
    }

    protected void post(String id, Double previous, Double current)
    {
        Mappet.EVENT_BUS.post(new StateChangedEvent(this, id, previous, current));
    }

    /* CRUD */

    public void add(String id, double value)
    {
        Double previous = this.values.get(id);

        this.values.put(id, (previous == null ? 0 : previous) + value);
        this.post(id, previous, value);
    }

    public void set(String id, double value)
    {
        Double previous = this.values.get(id);

        this.values.put(id, value);
        this.post(id, previous, value);
    }

    public double get(String id)
    {
        return this.values.containsKey(id) ? this.values.get(id) : 0;
    }

    public boolean reset(String id)
    {
        Double previous = this.values.get(id);

        this.values.remove(id);
        this.post(id, previous, null);

        return previous != null;
    }

    public void clear()
    {
        this.values.clear();
        this.post(null, null, null);
    }

    public void copy(States states)
    {
        this.values.clear();
        this.values.putAll(states.values);

        this.post(null, null, null);
    }

    /* Quest convenience methods */

    public void completeQuest(String id)
    {
        this.add(QUEST_PREFIX + id, 1);
    }

    public boolean wasQuestCompleted(String id)
    {
        return this.get(QUEST_PREFIX + id) > 0;
    }

    /* Faction convenience methods */

    public void addFactionScore(String id, int score, int defaultScore)
    {
        if (this.hasFaction(id))
        {
            this.add(FACTIONS_PREFIX + id, score);
        }
        else
        {
            this.set(FACTIONS_PREFIX + id, defaultScore + score);
        }
    }

    public void setFactionScore(String id, int score)
    {
        this.set(FACTIONS_PREFIX + id, score);
    }

    public int getFactionScore(String id)
    {
        return (int) this.get(FACTIONS_PREFIX + id);
    }

    public boolean clearFactionScore(String id)
    {
        return this.reset(FACTIONS_PREFIX + id);
    }

    public void clearAllFactionScores()
    {
        this.values.keySet().removeIf((key) -> key.startsWith(FACTIONS_PREFIX));
    }

    public boolean hasFaction(String id)
    {
        return this.values.containsKey(FACTIONS_PREFIX + id);
    }

    /* Dialogues convenience methods */

    public void readDialogue(String id)
    {
        this.add(DIALOGUE_PREFIX + id, 1);
    }

    public boolean hasReadDialogue(String id)
    {
        return this.getReadDialogueTimes(id) > 0;
    }

    public int getReadDialogueTimes(String id)
    {
        return (int) this.get(DIALOGUE_PREFIX + id);
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
        if (!this.file.exists())
        {
            return;
        }

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