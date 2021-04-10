package mchorse.mappet.api.utils.nodes;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public abstract class Node implements INBTSerializable<NBTTagCompound>
{
    private UUID id;

    /* Visual properties */
    public int x;
    public int y;

    public UUID getId()
    {
        return this.id;
    }

    public void setId(UUID id)
    {
        if (this.id == null)
        {
            this.id = id;
        }
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        if (this.id != null)
        {
            tag.setUniqueId("Id", this.id);
        }

        tag.setInteger("X", this.x);
        tag.setInteger("Y", this.y);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasUniqueId("Id"))
        {
            this.id = tag.getUniqueId("Id");
        }

        this.x = tag.getInteger("X");
        this.y = tag.getInteger("Y");
    }
}