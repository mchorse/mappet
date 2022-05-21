package mchorse.mappet.utils;

import mchorse.mappet.api.npcs.Npc;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.storage.WorldSavedData;

public class MappetNpcRespawner extends WorldSavedData
{

    public MappetNpcRespawner(String worldName)
    {
        super(worldName);

    }

    public void addNpc(Npc npc)
    {

    }

    @Override
    public void readFromNBT(NBTTagCompound nbtTagCompound)
    {

    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound nbtTagCompound)
    {
        return null;
    }
}
