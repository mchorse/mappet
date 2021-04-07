package mchorse.mappet.api.crafting;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import mchorse.mappet.utils.NBTToJson;
import mchorse.mclib.utils.JsonUtils;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.nio.charset.Charset;

public class CraftingManager
{
    public File folder;

    public CraftingManager(File folder)
    {
        this.folder = folder;
        this.folder.mkdirs();
    }

    public CraftingTable load(String name)
    {
        try
        {
            File file = this.getCraftingFile(name);
            String json = FileUtils.readFileToString(file, Charset.defaultCharset());
            JsonElement element = new JsonParser().parse(json);
            NBTTagCompound tag = (NBTTagCompound) NBTToJson.fromJson(element);
            CraftingTable table = new CraftingTable();

            table.deserializeNBT(tag);

            return table;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return null;
    }

    public boolean save(String name, CraftingTable recipe)
    {
        try
        {
            File file = this.getCraftingFile(name);
            NBTTagCompound tag = recipe.serializeNBT();
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

    private File getCraftingFile(String name)
    {
        return new File(this.folder, name + ".json");
    }
}