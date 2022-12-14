package mchorse.mappet.api.scripts.user.nbt;

import net.minecraft.nbt.NBTTagList;

/**
 * Interface that represents an NBT list tag
 */
public interface INBTList extends INBT
{
    /**
     * Get raw NBT tag list. <b>BEWARE:</b> you need to know the MCP
     * mappings in order to directly call methods on this instance!
     */
    public NBTTagList getNBTTagList();

    /**
     * Check whether this list has an element at given index (instead of
     * checking manually for index to be within 0..size-1 range)
     */
    public boolean has(int index);

    /**
     * Remove an element at given index
     */
    public void remove(int index);

    /**
     * Get byte (8-bit integer) value at given index
     */
    public byte getByte(int index);

    /**
     * Set byte (8-bit integer) value at given index
     */
    public void setByte(int index, byte value);

    /**
     * Add byte (8-bit integer) value at the end of the list
     */
    public void addByte(byte value);

    /**
     * Get short (16-bit integer) value at given index
     */
    public short getShort(int index);

    /**
     * Set short (16-bit integer) value at given index
     */
    public void setShort(int index, short value);

    /**
     * Add short (16-bit integer) value at the end of the list
     */
    public void addShort(short value);

    /**
     * Get integer (32-bit integer) value at given index
     */
    public int getInt(int index);

    /**
     * Set integer (32-bit integer) value at given index
     */
    public void setInt(int index, int value);

    /**
     * Add integer (32-bit integer) value at the end of the list
     */
    public void addInt(int value);

    /**
     * Get long (64-bit integer) value at given index
     */
    public long getLong(int index);

    /**
     * Set long (64-bit integer) value at given index
     */
    public void setLong(int index, long value);

    /**
     * Add long (64-bit integer) value at the end of the list
     */
    public void addLong(long value);

    /**
     * Get float (32-bit floating point number) value at given index
     */
    public float getFloat(int index);

    /**
     * Set float (32-bit floating point number) value at given index
     */
    public void setFloat(int index, float value);

    /**
     * Add float (32-bit floating point number) value at the end of the list
     */
    public void addFloat(float value);

    /**
     * Get double (64-bit floating point number) value at given index
     */
    public double getDouble(int index);

    /**
     * Set double (64-bit floating point number) value at given index
     */
    public void setDouble(int index, double value);

    /**
     * Add double (64-bit floating point number) value at the end of the list
     */
    public void addDouble(double value);

    /**
     * Get string value at given index
     */
    public String getString(int index);

    /**
     * Set string value at given index
     */
    public void setString(int index, String value);

    /**
     * Add string value at the end of the list
     */
    public void addString(String value);

    /**
     * Get boolean (true or false) value at given index
     */
    public boolean getBoolean(int index);

    /**
     * Set boolean (true or false) value at given index
     */
    public void setBoolean(int index, boolean value);

    /**
     * Add boolean (true or false) value at the end of the list
     */
    public void addBoolean(boolean value);

    /**
     * Get NBT compound at given index
     */
    public INBTCompound getCompound(int index);

    /**
     * Set NBT compound at given index
     */
    public void setCompound(int index, INBTCompound value);

    /**
     * Add NBT compound at the end of the list
     */
    public void addCompound(INBTCompound value);

    /**
     * Get NBT list at given index
     */
    public INBTList getList(int index);

    /**
     * Set NBT list at given index
     */
    public void setList(int index, INBTList value);

    /**
     * Add NBT list at the end of the list
     */
    public void addList(INBTList value);

    /**
     * Turns a NBT list into a Java array.
     *
     * <pre>{@code
     *     var tag = mappet.createCompound("{id:[0,2,4]}");
     *
     *     c.send(tag.get("id").toArray()[1]); // 2
     * }</pre>
     *
     * @return an array of the list's elements
     */
    public Object[] toArray();
}