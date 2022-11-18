package mchorse.mappet.api.utils.manager;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.io.File;
import java.util.Collection;

public interface IManager <T extends INBTSerializable<NBTTagCompound>>
{
    boolean exists(String name);

    public default T create(String id)
    {
        return this.create(id, null);
    }

    public T create(String id, NBTTagCompound tag);

    public T load(String id);

    public boolean save(String name, NBTTagCompound tag);

    public boolean rename(String id, String newId);

    public boolean delete(String name);

    public File getFolder();

    public Collection<String> getKeys();
}