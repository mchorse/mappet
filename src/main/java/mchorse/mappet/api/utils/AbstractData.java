package mchorse.mappet.api.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public abstract class AbstractData implements INBTSerializable<NBTTagCompound>, IID
{
    private String id;

    @Override
    public String getId()
    {
        return this.id;
    }

    @Override
    public void setId(String id)
    {
        if (this.id == null)
        {
            this.id = id;
        }
    }
}