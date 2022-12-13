package mchorse.mappet.api.scripts.code.nbt;

import mchorse.mappet.api.scripts.user.nbt.INBT;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.api.scripts.user.nbt.INBTList;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTPrimitive;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.common.util.Constants;

public class ScriptNBTList implements INBTList
{
    private NBTTagList list;

    public ScriptNBTList(NBTTagList list)
    {
        this.list = list == null ? new NBTTagList() : list;
    }

    @Override
    public NBTTagList getNBTTagList()
    {
        return this.list;
    }

    @Override
    public boolean isCompound()
    {
        return false;
    }

    @Override
    public boolean isList()
    {
        return true;
    }

    @Override
    public String stringify()
    {
        return this.list.toString();
    }

    /**
     * Just in case someone will use toString() instead of stringify()
     */
    @Override
    public String toString()
    {
        return this.list.toString();
    }

    @Override
    public boolean isEmpty()
    {
        return this.list.hasNoTags();
    }

    @Override
    public int size()
    {
        return this.list.tagCount();
    }

    @Override
    public INBT copy()
    {
        return new ScriptNBTList(this.list.copy());
    }

    @Override
    public void combine(INBT nbt)
    {
        if (nbt instanceof INBTList)
        {
            NBTTagList list = ((INBTList) nbt).getNBTTagList();

            if (this.list.getTagType() == list.getTagType())
            {
                for (int i = 0; i < list.tagCount(); i++)
                {
                    this.list.appendTag(list.get(i).copy());
                }
            }
        }
    }

    @Override
    public boolean isSame(INBT nbt)
    {
        if (nbt instanceof INBTList)
        {
            return this.list.equals(((INBTList) nbt).getNBTTagList());
        }

        return false;
    }

    /* INBTCompound implementation */

    @Override
    public boolean has(int index)
    {
        return index >= 0 && index < this.size();
    }

    @Override
    public void remove(int index)
    {
        this.list.removeTag(index);
    }

    @Override
    public byte getByte(int index)
    {
        NBTBase base = this.list.get(index);

        return base.getId() == Constants.NBT.TAG_BYTE ? ((NBTPrimitive) base).getByte() : (byte) 0;
    }

    @Override
    public void setByte(int index, byte value)
    {
        this.list.set(index, new NBTTagByte(value));
    }

    @Override
    public void addByte(byte value)
    {
        this.list.appendTag(new NBTTagByte(value));
    }

    @Override
    public short getShort(int index)
    {
        NBTBase base = this.list.get(index);

        return base.getId() == Constants.NBT.TAG_SHORT ? ((NBTPrimitive) base).getShort() : (short) 0;
    }

    @Override
    public void setShort(int index, short value)
    {
        this.list.set(index, new NBTTagShort(value));
    }

    @Override
    public void addShort(short value)
    {
        this.list.appendTag(new NBTTagShort(value));
    }

    @Override
    public int getInt(int index)
    {
        NBTBase base = this.list.get(index);

        return base.getId() == Constants.NBT.TAG_INT ? ((NBTPrimitive) base).getInt() : 0;
    }

    @Override
    public void setInt(int index, int value)
    {
        this.list.set(index, new NBTTagInt(value));
    }

    @Override
    public void addInt(int value)
    {
        this.list.appendTag(new NBTTagInt(value));
    }

    @Override
    public long getLong(int index)
    {
        NBTBase base = this.list.get(index);

        return base.getId() == Constants.NBT.TAG_LONG ? ((NBTPrimitive) base).getLong() : 0;
    }

    @Override
    public void setLong(int index, long value)
    {
        this.list.set(index, new NBTTagLong(value));
    }

    @Override
    public void addLong(long value)
    {
        this.list.appendTag(new NBTTagLong(value));
    }

    @Override
    public float getFloat(int index)
    {
        NBTBase base = this.list.get(index);

        return base.getId() == Constants.NBT.TAG_FLOAT ? ((NBTPrimitive) base).getFloat() : 0;
    }

    @Override
    public void setFloat(int index, float value)
    {
        this.list.set(index, new NBTTagFloat(value));
    }

    @Override
    public void addFloat(float value)
    {
        this.list.appendTag(new NBTTagFloat(value));
    }

    @Override
    public double getDouble(int index)
    {
        NBTBase base = this.list.get(index);

        return base.getId() == Constants.NBT.TAG_DOUBLE ? ((NBTPrimitive) base).getDouble() : 0;
    }

    @Override
    public void setDouble(int index, double value)
    {
        this.list.set(index, new NBTTagDouble(value));
    }

    @Override
    public void addDouble(double value)
    {
        this.list.appendTag(new NBTTagDouble(value));
    }

    @Override
    public String getString(int index)
    {
        NBTBase base = this.list.get(index);

        return base.getId() == Constants.NBT.TAG_STRING ? ((NBTTagString) base).getString() : "";
    }

    @Override
    public void setString(int index, String value)
    {
        this.list.set(index, new NBTTagString(value));
    }

    @Override
    public void addString(String value)
    {
        this.list.appendTag(new NBTTagString(value));
    }

    @Override
    public boolean getBoolean(int index)
    {
        NBTBase base = this.list.get(index);

        return base.getId() == Constants.NBT.TAG_BYTE && ((NBTPrimitive) base).getByte() != 0;
    }

    @Override
    public void setBoolean(int index, boolean value)
    {
        this.list.set(index, new NBTTagByte(value ? (byte) 1 : (byte) 0));
    }

    @Override
    public void addBoolean(boolean value)
    {
        this.list.appendTag(new NBTTagByte(value ? (byte) 1 : (byte) 0));
    }

    @Override
    public INBTCompound getCompound(int index)
    {
        return new ScriptNBTCompound(this.list.getCompoundTagAt(index));
    }

    @Override
    public void setCompound(int index, INBTCompound value)
    {
        this.list.set(index, value.getNBTTagCompound());
    }

    @Override
    public void addCompound(INBTCompound value)
    {
        this.list.appendTag(value.getNBTTagCompound());
    }

    @Override
    public INBTList getList(int index)
    {
        NBTBase base = this.list.get(index);

        return new ScriptNBTList(base.getId() == Constants.NBT.TAG_LIST ? (NBTTagList) base : null);
    }

    @Override
    public void setList(int index, INBTList value)
    {
        this.list.set(index, value.getNBTTagList());
    }

    @Override
    public void addList(INBTList value)
    {
        this.list.appendTag(value.getNBTTagList());
    }

    @Override
    public Object[] toArray()
    {
        Object[] array = new Object[this.list.tagCount()];

        for (int i = 0; i < this.list.tagCount(); i++)
        {
            array[i] = this.list.get(i);
        }

        return array;
    }
}