package mchorse.mappet.api.scripts.user.nbt;

import java.util.Set;

/**
 * Interface that represents an NBT compound tag
 */
public interface INBTCompound extends INBT
{
    /**
     * Check whether this NBT compound has a value by given key
     */
    public boolean has(String key);

    /**
     * Remove a value by given key
     */
    public void remove(String key);

    /**
     * Get all keys
     */
    public Set<String> keys();

    /* Assignment/query */

    /**
     * Get byte (8-bit integer) value by given key
     */
    public byte b(String key);

    /**
     * Set byte (8-bit integer) value by given key
     */
    public void b(String key, byte value);

    /**
     * Get short (16-bit integer) value by given key
     */
    public short s(String key);

    /**
     * Set short (16-bit integer) value by given key
     */
    public void s(String key, short value);

    /**
     * Get integer (32-bit integer) value by given key
     */
    public int i(String key);

    /**
     * Set integer (32-bit integer) value by given key
     */
    public void i(String key, int value);

    /**
     * Get long (64-bit integer) value by given key
     */
    public long l(String key);

    /**
     * Set long (64-bit integer) value by given key
     */
    public void l(String key, long value);

    /**
     * Get float (32-bit floating point number) value by given key
     */
    public float f(String key);

    /**
     * Set float (32-bit floating point number) value by given key
     */
    public void f(String key, float value);

    /**
     * Get double (64-bit floating point number) value by given key
     */
    public double d(String key);

    /**
     * Set double (64-bit floating point number) value by given key
     */
    public void d(String key, double value);

    /**
     * Get string value by given key
     */
    public String str(String key);

    /**
     * Set string value by given key
     */
    public void str(String key, String value);

    /**
     * Get boolean (true or false) value by given key
     */
    public boolean bool(String key);

    /**
     * Set boolean (true or false) value by given key
     */
    public void bool(String key, boolean value);

    /**
     * Get NBT compound by given key
     */
    public INBTCompound compound(String key);

    /**
     * Set NBT compound by given key
     */
    public void compound(String key, INBTCompound value);

    /**
     * Get NBT list by given key
     */
    public INBTList list(String key);

    /**
     * Set NBT list by given key
     */
    public void list(String key, INBTList value);
}