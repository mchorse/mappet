package mchorse.mappet.api.scripts.user.nbt;

import net.minecraft.nbt.NBTTagCompound;

import java.util.Set;

/**
 * Interface that represents an NBT compound tag
 */
public interface INBTCompound extends INBT
{
    /**
     * Get raw NBT tag compound. <b>BEWARE:</b> you need to know the MCP
     * mappings in order to directly call methods on this instance!
     */
    public NBTTagCompound getNBTTagCompound();

    /**
     * Deprecated version of {@link #getNBTTagCompound} to avoid errors
     * in existing scripts. Use the other method!
     */
    @Deprecated
    public default NBTTagCompound getNBTTagComound()
    {
        return this.getNBTTagCompound();
    }

    /**
     * Check whether this NBT compound has a value by given key.
     */
    public boolean has(String key);

    /**
     * Remove a value by given key.
     */
    public void remove(String key);

    /**
     * Get all keys.
     */
    public Set<String> keys();

    /* Assignment/query */

    /**
     * Get byte (8-bit integer) value by given key.
     */
    public byte getByte(String key);

    /**
     * Set byte (8-bit integer) value by given key.
     */
    public void setByte(String key, byte value);

    /**
     * Get short (16-bit integer) value by given key.
     */
    public short getShort(String key);

    /**
     * Set short (16-bit integer) value by given key.
     */
    public void setShort(String key, short value);

    /**
     * Get integer (32-bit integer) value by given key.
     */
    public int getInt(String key);

    /**
     * Set integer (32-bit integer) value by given key.
     */
    public void setInt(String key, int value);

    /**
     * Get long (64-bit integer) value by given key.
     */
    public long getLong(String key);

    /**
     * Set long (64-bit integer) value by given key.
     */
    public void setLong(String key, long value);

    /**
     * Get float (32-bit floating point number) value by given key.
     */
    public float getFloat(String key);

    /**
     * Set float (32-bit floating point number) value by given key.
     */
    public void setFloat(String key, float value);

    /**
     * Get double (64-bit floating point number) value by given key.
     */
    public double getDouble(String key);

    /**
     * Set double (64-bit floating point number) value by given key.
     */
    public void setDouble(String key, double value);

    /**
     * Get string value by given key.
     */
    public String getString(String key);

    /**
     * Set string value by given key.
     */
    public void setString(String key, String value);

    /**
     * Get boolean (true or false) value by given key.
     */
    public boolean getBoolean(String key);

    /**
     * Set boolean (true or false) value by given key.
     */
    public void setBoolean(String key, boolean value);

    /**
     * Get NBT compound by given key.
     */
    public INBTCompound getCompound(String key);

    /**
     * Set NBT compound by given key.
     */
    public void setCompound(String key, INBTCompound value);

    /**
     * Get NBT list by given key.
     */
    public INBTList getList(String key);

    /**
     * Set NBT list by given key.
     */
    public void setList(String key, INBTList value);

    /**
     * Set arbitrary NBT.
     *
     * <pre>{@code
     *    var compound = mappet.createCompound();
     *
     *    compound.setNBT("stack", '{id:"minecraft:diamond",Count:64b}');
     *
     *    // {stack:{id:"minecraft:diamond",Count:64b}}
     *    print(compound.stringify());
     * }</pre>
     *
     * @return Whether given NBT code was successfully was inserted.
     */
    public boolean setNBT(String key, String nbt);

    /**
     * get whatever inside the compound as a type of either
     * INBTCompound, INBTList,
     * String, int, double, float, long, short, byte, boolean
     * or null
     *
     * <pre>{@code
     *     var tag = mappet.createCompound("{id:\"minecraft:diamond_hoe\",Count:1b}");
     *     c.send(tag.get("id"))
     *     c.send(tag.get("Count"))
     * }</pre>
     *
     * @param key the key of the value
     * @return whatever inside the compound as a type of either
     */
    Object get(String key);

    /**
     * checks if a compound equals another compound, even if the order of the keys is different
     *
     * <pre>{@code
     *    var tag1 = mappet.createCompound("{id:\"minecraft:diamond_hoe\",Count:1b}");
     *    var tag2 = mappet.createCompound("{Count:1b,id:\"minecraft:diamond_hoe\"}");
     *    c.send(tag1.equals(tag2))
     * }</pre>
     *
     * @param compound the compound to compare to
     * @return whether the compound is equal to the other compound
     */
    boolean equals(INBTCompound compound);
}