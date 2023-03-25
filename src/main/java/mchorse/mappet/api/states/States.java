package mchorse.mappet.api.states;

import mchorse.mappet.Mappet;
import mchorse.mappet.events.StateChangedEvent;
import mchorse.mappet.utils.NBTToJsonLike;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

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

    public Map<String, Object> values = new HashMap<String, Object>();

    private File file;

    public States()
    {}

    public States(File file)
    {
        this.file = file;
    }

    protected void post(String id, Object previous, Object current)
    {
        Mappet.EVENT_BUS.post(new StateChangedEvent(this, id, previous, current));
    }

    /* CRUD */

    public void add(String id, double value)
    {
        Object previous = this.values.get(id);

        if (previous == null || previous instanceof Number)
        {
            this.values.put(id, (previous == null ? 0 : ((Number) previous).doubleValue()) + value);
            this.post(id, previous, value);
        }
    }

    public void setNumber(String id, double value)
    {
        Object previous = this.values.get(id);

        this.values.put(id, value);
        this.post(id, previous, value);
    }

    public void setString(String id, String value)
    {
        Object previous = this.values.get(id);

        this.values.put(id, value);
        this.post(id, previous, value);
    }

    public double getNumber(String id)
    {
        Object object = this.values.get(id);

        return object instanceof Number ? ((Number) object).doubleValue() : 0;
    }

    public boolean isNumber(String id)
    {
        Object object = this.values.get(id);

        return object instanceof Number;
    }

    public String getString(String id)
    {
        Object object = this.values.get(id);

        return object instanceof String ? (String) object : "";
    }

    public boolean isString(String id)
    {
        Object object = this.values.get(id);

        return object instanceof String;
    }

    public boolean reset(String id)
    {
        Object previous = this.values.remove(id);

        this.post(id, previous, null);

        return previous != null;
    }

    public boolean resetMasked(String id)
    {
        if (id.trim().equals("*"))
        {
            boolean wasEmpty = this.values.isEmpty();

            if (!wasEmpty)
            {
                this.clear();
            }

            return !wasEmpty;
        }

        if (id.contains("*"))
        {
            id = id.replaceAll("\\*", ".*");

            Pattern pattern = Pattern.compile("^" + id + "$");
            int size = this.values.size();

            this.values.keySet().removeIf(key -> pattern.matcher(key).matches());

            if (this.values.size() != size)
            {
                this.post(null, null, null);

                return true;
            }

            return false;
        }

        return this.reset(id);
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

    public int getQuestCompletedTimes(String id)
    {
        return (int) this.getNumber(QUEST_PREFIX + id);
    }

    public boolean wasQuestCompleted(String id)
    {
        return this.getQuestCompletedTimes(id) > 0;
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
            this.setNumber(FACTIONS_PREFIX + id, defaultScore + score);
        }
    }

    public void setFactionScore(String id, int score)
    {
        this.setNumber(FACTIONS_PREFIX + id, score);
    }

    public int getFactionScore(String id)
    {
        return (int) this.getNumber(FACTIONS_PREFIX + id);
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

    public Set<String> getFactionNames()
    {
        Set<String> factionNames = new HashSet<>();
        for (String key : this.values.keySet())
        {
            if (key.startsWith(FACTIONS_PREFIX))
            {
                factionNames.add(key.replace(FACTIONS_PREFIX, ""));
            }
        }
        return factionNames;
    }

    /* Dialogues convenience methods */

    public void readDialogue(String id, String marker)
    {
        this.add(this.getDialogueId(id, marker), 1);
    }

    public boolean hasReadDialogue(String id, String marker)
    {
        return this.getReadDialogueTimes(id, marker) > 0;
    }

    public int getReadDialogueTimes(String id, String marker)
    {
        return (int) this.getNumber(this.getDialogueId(id, marker));
    }

    private String getDialogueId(String id, String marker)
    {
        id = DIALOGUE_PREFIX + id;

        if (marker != null && !marker.isEmpty())
        {
            id += ":" + marker;
        }

        return id;
    }

    /* NBT */

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        for (Map.Entry<String, Object> entry : this.values.entrySet())
        {
            if (entry.getValue() instanceof Number)
            {
                tag.setDouble(entry.getKey(), ((Number) entry.getValue()).doubleValue());
            }
            else if (entry.getValue() instanceof String)
            {
                tag.setString(entry.getKey(), (String) entry.getValue());
            }
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.values.clear();

        for (String key : tag.getKeySet())
        {
            NBTBase base = tag.getTag(key);

            if (base.getId() == Constants.NBT.TAG_STRING)
            {
                this.values.put(key, ((NBTTagString) base).getString());
            }
            else if (base instanceof NBTPrimitive)
            {
                this.values.put(key, ((NBTPrimitive) base).getDouble());
            }
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
            this.deserializeNBT(NBTToJsonLike.read(this.file));
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
            NBTToJsonLike.write(this.file, this.serializeNBT());

            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return false;
    }
}