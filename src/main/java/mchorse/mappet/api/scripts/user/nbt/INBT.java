package mchorse.mappet.api.scripts.user.nbt;

/**
 * Common interface for NBT data types.
 */
public interface INBT
{
    /**
     * Check whether this NBT data is an NBT compound.
     */
    public boolean isCompound();

    /**
     * Check whether this NBT data is an NBT list.
     */
    public boolean isList();

    /**
     * Convert this NBT structure to string.
     */
    public String stringify();

    /**
     * Check whether this NBT tag is empty.
     */
    public boolean isEmpty();

    /**
     * Get the size (amount of elements) in this NBT tag.
     */
    public int size();

    /**
     * Create a copy of this NBT tag.
     */
    public INBT copy();

    /**
     * Add given NBT data's values on top of this one.
     */
    public void combine(INBT nbt);

    /**
     * Check whether given NBT tag is same as this one.
     */
    public boolean isSame(INBT nbt);
}