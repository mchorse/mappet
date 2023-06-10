package mchorse.mappet.api.scripts.code.entities.ai.rotations;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RotationDataStorage extends WorldSavedData
{
    public static final String ROTATION_DATA_KEY = "mappet_rotation_data";
    private Map<UUID, RotationData> rotationDataMap = new HashMap<>();

    public RotationDataStorage()
    {
        super(ROTATION_DATA_KEY);
    }

    public RotationDataStorage(String name)
    {
        super(name);
    }

    public static RotationDataStorage getRotationDataStorage(World world)
    {
        MapStorage storage = world.getMapStorage();
        RotationDataStorage rotationDataStorage = (RotationDataStorage) storage.getOrLoadData(RotationDataStorage.class, ROTATION_DATA_KEY);

        if (rotationDataStorage == null)
        {
            rotationDataStorage = new RotationDataStorage();
            storage.setData(ROTATION_DATA_KEY, rotationDataStorage);
        }
        return rotationDataStorage;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt)
    {
        rotationDataMap.clear();
        NBTTagList rotationDataList = nbt.getTagList("rotationDataList", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < rotationDataList.tagCount(); i++)
        {
            NBTTagCompound rotationDataCompound = rotationDataList.getCompoundTagAt(i);
            UUID entityId = UUID.fromString(rotationDataCompound.getString("entityId"));
            float yaw = rotationDataCompound.getFloat("yaw");
            float pitch = rotationDataCompound.getFloat("pitch");
            float yawHead = rotationDataCompound.getFloat("yawHead");
            rotationDataMap.put(entityId, new RotationData(yaw, pitch, yawHead));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbt)
    {
        NBTTagList rotationDataList = new NBTTagList();
        for (Map.Entry<UUID, RotationData> entry : rotationDataMap.entrySet())
        {
            NBTTagCompound rotationDataCompound = new NBTTagCompound();
            rotationDataCompound.setString("entityId", entry.getKey().toString());
            rotationDataCompound.setFloat("yaw", entry.getValue().yaw);
            rotationDataCompound.setFloat("pitch", entry.getValue().pitch);
            rotationDataCompound.setFloat("yawHead", entry.getValue().yawHead);
            rotationDataList.appendTag(rotationDataCompound);
        }
        nbt.setTag("rotationDataList", rotationDataList);
        return nbt;
    }

    public void addRotationData(UUID entityId, float yaw, float pitch, float yawHead)
    {
        rotationDataMap.put(entityId, new RotationData(yaw, pitch, yawHead));
        markDirty();
    }

    public void removeRotationData(UUID entityId)
    {
        rotationDataMap.remove(entityId);
        markDirty();
    }

    public RotationData getRotationData(UUID entityId)
    {
        return rotationDataMap.get(entityId);
    }


    public static class RotationData
    {
        public final float yaw;
        public final float pitch;
        public final float yawHead;

        public RotationData(float yaw, float pitch, float yawHead)
        {
            this.yaw = yaw;
            this.pitch = pitch;
            this.yawHead = yawHead;
        }
    }
}
