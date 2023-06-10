package mchorse.mappet.api.scripts.code.entities.ai.repeatingCommand;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class RepeatingCommandDataStorage extends WorldSavedData
{
    public static final String REPEATING_COMMAND_DATA_KEY = "mappet_repeating_command_data";

    private Map<UUID, List<RepeatingCommandData>> repeatingCommandDataMap = new HashMap<>();

    public RepeatingCommandDataStorage()
    {
        super(REPEATING_COMMAND_DATA_KEY);
    }

    public RepeatingCommandDataStorage(String name)
    {
        super(name);
    }

    public static RepeatingCommandDataStorage getRepeatingCommandDataStorage(World world)
    {
        MapStorage storage = world.getMapStorage();
        RepeatingCommandDataStorage repeatingCommandDataStorage = (RepeatingCommandDataStorage) storage.getOrLoadData(RepeatingCommandDataStorage.class, REPEATING_COMMAND_DATA_KEY);

        if (repeatingCommandDataStorage == null)
        {
            repeatingCommandDataStorage = new RepeatingCommandDataStorage();

            storage.setData(REPEATING_COMMAND_DATA_KEY, repeatingCommandDataStorage);
        }
        return repeatingCommandDataStorage;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        repeatingCommandDataMap.clear();

        NBTTagList repeatingCommandDataList = nbt.getTagList("repeatingCommandDataList", Constants.NBT.TAG_COMPOUND);

        for (int i = 0; i < repeatingCommandDataList.tagCount(); i++)
        {
            NBTTagCompound repeatingCommandDataCompound = repeatingCommandDataList.getCompoundTagAt(i);
            UUID entityId = UUID.fromString(repeatingCommandDataCompound.getString("entityId"));
            String command = repeatingCommandDataCompound.getString("command");
            int frequency = repeatingCommandDataCompound.getInteger("frequency");

            repeatingCommandDataMap.computeIfAbsent(entityId, k -> new ArrayList<>()).add(new RepeatingCommandData(command, frequency));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        NBTTagList repeatingCommandDataList = new NBTTagList();

        for (Map.Entry<UUID, List<RepeatingCommandData>> entry : repeatingCommandDataMap.entrySet())
        {
            for (RepeatingCommandData data : entry.getValue())
            {
                NBTTagCompound repeatingCommandDataCompound = new NBTTagCompound();

                repeatingCommandDataCompound.setString("entityId", entry.getKey().toString());
                repeatingCommandDataCompound.setString("command", data.command);
                repeatingCommandDataCompound.setInteger("frequency", data.frequency);
                repeatingCommandDataList.appendTag(repeatingCommandDataCompound);
            }
        }

        nbt.setTag("repeatingCommandDataList", repeatingCommandDataList);

        return nbt;
    }

    public void addRepeatingCommandData(UUID entityId, String command, int frequency)
    {
        repeatingCommandDataMap.computeIfAbsent(entityId, k -> new ArrayList<>()).add(new RepeatingCommandData(command, frequency));
        markDirty();
    }

    public void removeRepeatingCommandData(UUID entityId)
    {
        repeatingCommandDataMap.remove(entityId);
        markDirty();
    }

    public List<RepeatingCommandData> getRepeatingCommandData(UUID entityId)
    {
        return repeatingCommandDataMap.get(entityId);
    }

    public static class RepeatingCommandData
    {
        public final String command;

        public final int frequency;

        public RepeatingCommandData(String command, int frequency)
        {
            this.command = command;
            this.frequency = frequency;
        }
    }

    public void removeSpecificRepeatingCommandData(UUID entityId, String command)
    {
        List<RepeatingCommandData> commandDataList = repeatingCommandDataMap.get(entityId);

        if (commandDataList != null)
        {
            commandDataList.removeIf(data -> data.command.equals(command));

            if (commandDataList.isEmpty())
            {
                repeatingCommandDataMap.remove(entityId);
            }

            markDirty();
        }
    }
}
