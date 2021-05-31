package mchorse.mappet.api.scripts.user.nbt;

/**
 * Interface that represents an NBT list tag
 */
public interface INBTList extends INBT
{
    /**
     * Remove an element at given index
     */
    public void remove(int index);

    /**
     * Get byte (8-bit integer) value at given index
     */
    public byte b(int index);

    /**
     * Set byte (8-bit integer) value at given index
     */
    public void b(int index, byte value);

    /**
     * Add byte (8-bit integer) value at the end of the list
     */
    public void addB(byte value);

    /**
     * Get short (16-bit integer) value at given index
     */
    public short s(int index);

    /**
     * Set short (16-bit integer) value at given index
     */
    public void s(int index, short value);

    /**
     * Add short (16-bit integer) value at the end of the list
     */
    public void addS(short value);

    /**
     * Get integer (32-bit integer) value at given index
     */
    public int i(int index);

    /**
     * Set integer (32-bit integer) value at given index
     */
    public void i(int index, int value);

    /**
     * Add integer (32-bit integer) value at the end of the list
     */
    public void addI(int value);

    /**
     * Get long (64-bit integer) value at given index
     */
    public long l(int index);

    /**
     * Set long (64-bit integer) value at given index
     */
    public void l(int index, long value);

    /**
     * Add long (64-bit integer) value at the end of the list
     */
    public void addL(long value);

    /**
     * Get float (32-bit floating point number) value at given index
     */
    public float f(int index);

    /**
     * Set float (32-bit floating point number) value at given index
     */
    public void f(int index, float value);

    /**
     * Add float (32-bit floating point number) value at the end of the list
     */
    public void addF(float value);

    /**
     * Get double (64-bit floating point number) value at given index
     */
    public double d(int index);

    /**
     * Set double (64-bit floating point number) value at given index
     */
    public void d(int index, double value);

    /**
     * Add double (64-bit floating point number) value at the end of the list
     */
    public void addD(double value);

    /**
     * Get string value at given index
     */
    public String str(int index);

    /**
     * Set string value at given index
     */
    public void str(int index, String value);

    /**
     * Add string value at the end of the list
     */
    public void addStr(String value);

    /**
     * Get boolean (true or false) value at given index
     */
    public boolean bool(int index);

    /**
     * Set boolean (true or false) value at given index
     */
    public void bool(int index, boolean value);

    /**
     * Add boolean (true or false) value at the end of the list
     */
    public void addBool(boolean value);

    /**
     * Get NBT compound at given index
     */
    public INBTCompound compound(int index);

    /**
     * Set NBT compound at given index
     */
    public void compound(int index, INBTCompound value);

    /**
     * Add NBT compound at the end of the list
     */
    public void addCompound(INBTCompound value);

    /**
     * Get NBT list at given index
     */
    public INBTList list(int index);

    /**
     * Set NBT list at given index
     */
    public void list(int index, INBTList value);

    /**
     * Add NBT list at the end of the list
     */
    public void addList(INBTList value);
}