package mchorse.mappet.api.scripts.code.nbt;

import mchorse.mappet.api.scripts.user.nbt.INBT;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.api.scripts.user.nbt.INBTList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.Set;

public class ScriptNBTCompound implements INBTCompound
{
    private NBTTagCompound tag;

    public ScriptNBTCompound(NBTTagCompound tag)
    {
        this.tag = tag == null ? new NBTTagCompound() : tag;
    }

    public NBTTagCompound getNBTCompound()
    {
        return this.tag;
    }

    @Override
    public boolean isCompound()
    {
        return true;
    }

    @Override
    public boolean isList()
    {
        return false;
    }

    @Override
    public String stringify()
    {
        return this.tag.toString();
    }

    @Override
    public boolean isEmpty()
    {
        return this.tag.hasNoTags();
    }

    @Override
    public int size()
    {
        return this.tag.getSize();
    }

    @Override
    public void combine(INBT nbt)
    {
        if (nbt instanceof ScriptNBTCompound)
        {
            this.tag.merge(((ScriptNBTCompound) nbt).tag);
        }
    }

    @Override
    public boolean isSame(INBT nbt)
    {
        if (nbt instanceof ScriptNBTCompound)
        {
            return this.tag.equals(((ScriptNBTCompound) nbt).tag);
        }

        return false;
    }

    @Override
    public boolean has(String key)
    {
        return this.tag.hasKey(key);
    }

    @Override
    public void remove(String key)
    {
        this.tag.removeTag(key);
    }

    @Override
    public Set<String> keys()
    {
        return this.tag.getKeySet();
    }

    @Override
    public INBTCompound copy()
    {
        return new ScriptNBTCompound(this.tag.copy());
    }

    /* INBTCompound implementation */

    @Override
    public byte b(String key)
    {
        return this.tag.getByte(key);
    }

    @Override
    public void b(String key, byte value)
    {
        this.tag.setByte(key, value);
    }

    @Override
    public short s(String key)
    {
        return this.tag.getShort(key);
    }

    @Override
    public void s(String key, short value)
    {
        this.tag.setShort(key, value);
    }

    @Override
    public int i(String key)
    {
        return this.tag.getInteger(key);
    }

    @Override
    public void i(String key, int value)
    {
        this.tag.setInteger(key, value);
    }

    @Override
    public long l(String key)
    {
        return this.tag.getLong(key);
    }

    @Override
    public void l(String key, long value)
    {
        this.tag.setLong(key, value);
    }

    @Override
    public float f(String key)
    {
        return this.tag.getFloat(key);
    }

    @Override
    public void f(String key, float value)
    {
        this.tag.setFloat(key, value);
    }

    @Override
    public double d(String key)
    {
        return this.tag.getDouble(key);
    }

    @Override
    public void d(String key, double value)
    {
        this.tag.setDouble(key, value);
    }

    @Override
    public String str(String key)
    {
        return this.tag.getString(key);
    }

    @Override
    public void str(String key, String value)
    {
        this.tag.setString(key, value);
    }

    @Override
    public boolean bool(String key)
    {
        return this.tag.getBoolean(key);
    }

    @Override
    public void bool(String key, boolean value)
    {
        this.tag.setBoolean(key, value);
    }

    @Override
    public INBTCompound compound(String key)
    {
        return new ScriptNBTCompound(this.tag.getCompoundTag(key));
    }

    @Override
    public void compound(String key, INBTCompound value)
    {
        this.tag.setTag(key, ((ScriptNBTCompound) value).tag);
    }

    @Override
    public INBTList list(String key)
    {
        NBTBase tag = this.tag.getTag(key);

        return new ScriptNBTList(tag instanceof NBTTagList ? (NBTTagList) tag : new NBTTagList());
    }

    @Override
    public void list(String key, INBTList value)
    {
        this.tag.setTag(key, ((ScriptNBTList) value).getNBTList());
    }
}