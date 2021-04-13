package mchorse.mappet.api.utils;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Collection;

public interface IManager <T extends INBTSerializable<NBTTagCompound>>
{
    public default T create()
    {
        return this.create(null);
    }

    public T create(NBTTagCompound tag);

    public T load(String id);

    public boolean save(String name, NBTTagCompound tag);

    public boolean rename(String id, String newId);

    public boolean delete(String name);

    public Collection<String> getKeys();
}