package mchorse.mappet.api.states;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import mchorse.mclib.utils.JsonUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

/**
 * States allow to store global values of the world that can be
 * used in dialogues, crafting tables, events and et cetera
 * to control logic and store arbitrary numerical values
 */
public class States
{
    public Map<String, Double> values = new HashMap<String, Double>();

    private File file;

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