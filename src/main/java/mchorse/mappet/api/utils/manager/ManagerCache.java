package mchorse.mappet.api.utils.manager;

import net.minecraft.nbt.NBTTagCompound;

public class ManagerCache
{
    public NBTTagCompound tag;
    public long lastUpdated;
    public long lastUsed;

    public ManagerCache(NBTTagCompound tag, long lastUpdated)
    {
        this.tag = tag;
        this.lastUpdated = lastUpdated;
        this.update();
    }

    public void update()
    {
        this.lastUsed = System.currentTimeMillis();
    }
}