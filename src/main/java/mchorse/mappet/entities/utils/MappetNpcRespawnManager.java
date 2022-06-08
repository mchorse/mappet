package mchorse.mappet.entities.utils;

import mchorse.mappet.Mappet;
import mchorse.mappet.entities.EntityNpc;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.List;

public class MappetNpcRespawnManager extends WorldSavedData
{
    public static final String DATA_NAME = Mappet.MOD_ID + "_RespawnData";
    public final List<DiedNpcHolder> diedNpcHolders = new ArrayList<>();
    public final List<DiedNpcHolder> deleteCache = new ArrayList<>();
    public MinecraftServer server;

    public MappetNpcRespawnManager(String mapName)
    {
        super(mapName);
    }

    public void onTick()
    {
        for (DiedNpcHolder diedNpcHolder : this.diedNpcHolders)
        {
            if (diedNpcHolder.respawnTime <= server.getWorld(diedNpcHolder.worldID).getTotalWorldTime())
            {
                this.respawnNpc(diedNpcHolder);
            }
        }

        this.removeSpawnedNpcs();
        this.markDirty();
    }

    public void addDiedNpc(EntityNpc npc)
    {
        long respawnTime = npc.world.getTotalWorldTime() + npc.getState().respawnDelay;
        double posX = npc.getState().respawnOnCoordinates ? npc.getState().respawnPosX : npc.posX;
        double posY = npc.getState().respawnOnCoordinates ? npc.getState().respawnPosY : npc.posY;
        double posZ = npc.getState().respawnOnCoordinates ? npc.getState().respawnPosZ : npc.posZ;

        this.diedNpcHolders.add(new DiedNpcHolder(npc, respawnTime, posX, posY, posZ));
        this.markDirty();
    }

    public void respawnNpc(DiedNpcHolder diedNpcHolder)
    {
        diedNpcHolder.spawn(server.getWorld(diedNpcHolder.worldID));

        deleteCache.add(diedNpcHolder);
    }

    public void removeSpawnedNpcs()
    {
        for (DiedNpcHolder diedNpcHolder : this.deleteCache)
        {
            this.diedNpcHolders.remove(diedNpcHolder);
        }

        clearDeleteCache();
    }

    public void clearDeleteCache()
    {
        this.deleteCache.clear();
    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {
        NBTTagList tagList = nbtTagCompound.getTagList("diedNpcHolders", 10);

        for (NBTBase tag : tagList)
        {
            NBTTagCompound compound = (NBTTagCompound) tag;
            NBTTagCompound nbt = compound.getCompoundTag("nbt");

            String uuid = compound.getString("uuid");
            long respawnTime = compound.getLong("respawnTime");
            int worldID = compound.getInteger("worldID");
            double posX = compound.getDouble("posX");
            double posY = compound.getDouble("posY");
            double posZ = compound.getDouble("posZ");

            diedNpcHolders.add(new DiedNpcHolder(nbt, uuid, respawnTime, worldID, posX, posY, posZ));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
    {
        NBTTagList tagList = new NBTTagList();

        for (DiedNpcHolder diedNpcHolder : diedNpcHolders)
        {
            NBTTagCompound tag = new NBTTagCompound();

            tag.setTag("nbt", diedNpcHolder.nbt);
            tag.setString("uuid", diedNpcHolder.uuid);
            tag.setLong("respawnTime", diedNpcHolder.respawnTime);
            tag.setInteger("worldID", diedNpcHolder.worldID);
            tag.setDouble("posX", diedNpcHolder.posX);
            tag.setDouble("posY", diedNpcHolder.posY);
            tag.setDouble("posZ", diedNpcHolder.posZ);

            tagList.appendTag(tag);
        }

        nbtTagCompound.setTag("diedNpcHolders", tagList);

        return nbtTagCompound;
    }

    public static MappetNpcRespawnManager get(World world)
    {
        MappetNpcRespawnManager data = (MappetNpcRespawnManager) world.getMapStorage().getOrLoadData(MappetNpcRespawnManager.class, DATA_NAME);

        if (data == null)
        {
            data = new MappetNpcRespawnManager(DATA_NAME);
            world.getMapStorage().setData(DATA_NAME, data);
        }

        data.server = world.getMinecraftServer();

        return data;
    }
}
