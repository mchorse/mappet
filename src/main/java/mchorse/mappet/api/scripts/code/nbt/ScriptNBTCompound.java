package mchorse.mappet.api.scripts.code.nbt;

import mchorse.mappet.api.scripts.user.nbt.INBT;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.api.scripts.user.nbt.INBTList;
import net.minecraft.nbt.*;

import java.util.Set;

public class ScriptNBTCompound implements INBTCompound
{
    private NBTTagCompound tag;

    public ScriptNBTCompound(NBTTagCompound tag)
    {
        this.tag = tag == null ? new NBTTagCompound() : tag;
    }

    @Override
    public NBTTagCompound getNBTTagCompound()
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

    /**
     * Just in case someone will use toString() instead of stringify()
     */
    @Override
    public String toString()
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
    public byte getByte(String key)
    {
        return this.tag.getByte(key);
    }

    @Override
    public void setByte(String key, byte value)
    {
        this.tag.setByte(key, value);
    }

    @Override
    public short getShort(String key)
    {
        return this.tag.getShort(key);
    }

    @Override
    public void setShort(String key, short value)
    {
        this.tag.setShort(key, value);
    }

    @Override
    public int getInt(String key)
    {
        return this.tag.getInteger(key);
    }

    @Override
    public void setInt(String key, int value)
    {
        this.tag.setInteger(key, value);
    }

    @Override
    public long getLong(String key)
    {
        return this.tag.getLong(key);
    }

    @Override
    public void setLong(String key, long value)
    {
        this.tag.setLong(key, value);
    }

    @Override
    public float getFloat(String key)
    {
        return this.tag.getFloat(key);
    }

    @Override
    public void setFloat(String key, float value)
    {
        this.tag.setFloat(key, value);
    }

    @Override
    public double getDouble(String key)
    {
        return this.tag.getDouble(key);
    }

    @Override
    public void setDouble(String key, double value)
    {
        this.tag.setDouble(key, value);
    }

    @Override
    public String getString(String key)
    {
        return this.tag.getString(key);
    }

    @Override
    public void setString(String key, String value)
    {
        this.tag.setString(key, value);
    }

    @Override
    public boolean getBoolean(String key)
    {
        return this.tag.getBoolean(key);
    }

    @Override
    public void setBoolean(String key, boolean value)
    {
        this.tag.setBoolean(key, value);
    }

    @Override
    public INBTCompound getCompound(String key)
    {
        return new ScriptNBTCompound(this.tag.getCompoundTag(key));
    }

    @Override
    public void setCompound(String key, INBTCompound value)
    {
        this.tag.setTag(key, ((ScriptNBTCompound) value).tag);
    }

    @Override
    public INBTList getList(String key)
    {
        NBTBase tag = this.tag.getTag(key);

        return new ScriptNBTList(tag instanceof NBTTagList ? (NBTTagList) tag : new NBTTagList());
    }

    @Override
    public void setList(String key, INBTList value)
    {
        this.tag.setTag(key, value.getNBTTagList());
    }

    @Override
    public boolean setNBT(String key, String nbt)
    {
        try
        {
            NBTTagCompound tag = JsonToNBT.getTagFromJson("{data:" + nbt + "}");

            this.tag.setTag(key, tag.getTag("data"));

            return true;
        }
        catch (Exception e)
        {}

        return false;
    }

    @Override
    public Object get(String key)
    {
        NBTBase tag = this.tag.getTag(key);

        if (tag instanceof NBTTagCompound)
        {
            return new ScriptNBTCompound((NBTTagCompound) tag);
        }
        else if (tag instanceof NBTTagList)
        {
            return new ScriptNBTList((NBTTagList) tag);
        }
        else if (tag instanceof net.minecraft.nbt.NBTTagString)
        {
            return getString(key);
        }
        else if (tag instanceof NBTTagInt)
        {
            return getInt(key);
        }
        else if (tag instanceof NBTTagDouble)
        {
            return getDouble(key);
        }
        else if (tag instanceof NBTTagFloat)
        {
            return getFloat(key);
        }
        else if (tag instanceof NBTTagLong)
        {
            return getLong(key);
        }
        else if (tag instanceof NBTTagShort)
        {
            return getShort(key);
        }
        else if (tag instanceof NBTTagByte)
        {
            return getByte(key);
        }
        else if (tag instanceof NBTTagEnd)
        {
            return null;
        }

        return null;
    }

    @Override
    public boolean equals(INBTCompound compound)
    {
        return this.tag.equals(((ScriptNBTCompound) compound).tag);
    }

}