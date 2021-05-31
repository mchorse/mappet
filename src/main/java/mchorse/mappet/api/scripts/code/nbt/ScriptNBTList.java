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

    public NBTTagList getNBTList()
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
        if (nbt instanceof ScriptNBTList)
        {
            NBTTagList list = ((ScriptNBTList) nbt).getNBTList();

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
        if (nbt instanceof ScriptNBTList)
        {
            return this.list.equals(((ScriptNBTList) nbt).getNBTList());
        }

        return false;
    }

    /* INBTCompound implementation */

    @Override
    public void remove(int index)
    {
        this.list.removeTag(index);
    }

    @Override
    public byte b(int index)
    {
        NBTBase base = this.list.get(index);

        return base.getId() == Constants.NBT.TAG_BYTE ? ((NBTPrimitive) base).getByte() : (byte) 0;
    }

    @Override
    public void b(int index, byte value)
    {
        this.list.set(index, new NBTTagByte(value));
    }

    @Override
    public void addB(byte value)
    {
        this.list.appendTag(new NBTTagByte(value));
    }

    @Override
    public short s(int index)
    {
        NBTBase base = this.list.get(index);

        return base.getId() == Constants.NBT.TAG_SHORT ? ((NBTPrimitive) base).getShort() : (short) 0;
    }

    @Override
    public void s(int index, short value)
    {
        this.list.set(index, new NBTTagShort(value));
    }

    @Override
    public void addS(short value)
    {
        this.list.appendTag(new NBTTagShort(value));
    }

    @Override
    public int i(int index)
    {
        NBTBase base = this.list.get(index);

        return base.getId() == Constants.NBT.TAG_INT ? ((NBTPrimitive) base).getInt() : 0;
    }

    @Override
    public void i(int index, int value)
    {
        this.list.set(index, new NBTTagInt(value));
    }

    @Override
    public void addI(int value)
    {
        this.list.appendTag(new NBTTagInt(value));
    }

    @Override
    public long l(int index)
    {
        NBTBase base = this.list.get(index);

        return base.getId() == Constants.NBT.TAG_LONG ? ((NBTPrimitive) base).getLong() : 0;
    }

    @Override
    public void l(int index, long value)
    {
        this.list.set(index, new NBTTagLong(value));
    }

    @Override
    public void addL(long value)
    {
        this.list.appendTag(new NBTTagLong(value));
    }

    @Override
    public float f(int index)
    {
        NBTBase base = this.list.get(index);

        return base.getId() == Constants.NBT.TAG_FLOAT ? ((NBTPrimitive) base).getFloat() : 0;
    }

    @Override
    public void f(int index, float value)
    {
        this.list.set(index, new NBTTagFloat(value));
    }

    @Override
    public void addF(float value)
    {
        this.list.appendTag(new NBTTagFloat(value));
    }

    @Override
    public double d(int index)
    {
        NBTBase base = this.list.get(index);

        return base.getId() == Constants.NBT.TAG_DOUBLE ? ((NBTPrimitive) base).getDouble() : 0;
    }

    @Override
    public void d(int index, double value)
    {
        this.list.set(index, new NBTTagDouble(value));
    }

    @Override
    public void addD(double value)
    {
        this.list.appendTag(new NBTTagDouble(value));
    }

    @Override
    public String str(int index)
    {
        NBTBase base = this.list.get(index);

        return base.getId() == Constants.NBT.TAG_STRING ? ((NBTTagString) base).getString() : "";
    }

    @Override
    public void str(int index, String value)
    {
        this.list.set(index, new NBTTagString(value));
    }

    @Override
    public void addStr(String value)
    {
        this.list.appendTag(new NBTTagString(value));
    }

    @Override
    public boolean bool(int index)
    {
        NBTBase base = this.list.get(index);

        return base.getId() == Constants.NBT.TAG_BYTE && ((NBTPrimitive) base).getByte() != 0;
    }

    @Override
    public void bool(int index, boolean value)
    {
        this.list.set(index, new NBTTagByte(value ? (byte) 1 : (byte) 0));
    }

    @Override
    public void addBool(boolean value)
    {
        this.list.appendTag(new NBTTagByte(value ? (byte) 1 : (byte) 0));
    }

    @Override
    public INBTCompound compound(int index)
    {
        return new ScriptNBTCompound(this.list.getCompoundTagAt(index));
    }

    @Override
    public void compound(int index, INBTCompound value)
    {
        this.list.set(index, ((ScriptNBTCompound) value).getNBTCompound());
    }

    @Override
    public void addCompound(INBTCompound value)
    {
        this.list.appendTag(((ScriptNBTCompound) value).getNBTCompound());
    }

    @Override
    public INBTList list(int index)
    {
        NBTBase base = this.list.get(index);

        return new ScriptNBTList(base.getId() == Constants.NBT.TAG_LIST ? (NBTTagList) base : null);
    }

    @Override
    public void list(int index, INBTList value)
    {
        this.list.set(index, ((ScriptNBTList) value).getNBTList());
    }

    @Override
    public void addList(INBTList value)
    {
        this.list.appendTag(((ScriptNBTList) value).getNBTList());
    }
}