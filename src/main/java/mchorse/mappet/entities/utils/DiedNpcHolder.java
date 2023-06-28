package mchorse.mappet.entities.utils;

import mchorse.mappet.entities.EntityNpc;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

import java.util.UUID;

public class DiedNpcHolder
{
    public NBTTagCompound nbt;
    public long respawnTime;
    public String uuid;
    public int worldID;
    public double posX;
    public double posY;
    public double posZ;

    public DiedNpcHolder(EntityNpc npc, long respawnTime, double posX, double posY, double posZ)
    {
        this.nbt = npc.writeToNBT(new NBTTagCompound());
        this.uuid = npc.getUniqueID().toString();
        this.respawnTime = respawnTime;
        this.worldID = npc.dimension;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public DiedNpcHolder(NBTTagCompound nbt, String uuid, long respawnTime, int worldID, double posX, double posY, double posZ)
    {
        this.nbt = nbt;
        this.uuid = uuid;
        this.respawnTime = respawnTime;
        this.worldID = worldID;
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
    }

    public void spawn(World world)
    {
        EntityNpc npc = new EntityNpc(world);

        npc.readEntityFromNBT(this.nbt);
        npc.setPosition(this.posX, this.posY, this.posZ);
        npc.setHealth(npc.getMaxHealth());

        if (npc.getState().respawnSaveUUID.get())
        {
            npc.setUniqueId(UUID.fromString(this.uuid));
        }

        world.spawnEntity(npc);
        npc.getState().triggerRespawn.trigger(npc);
    }
}
