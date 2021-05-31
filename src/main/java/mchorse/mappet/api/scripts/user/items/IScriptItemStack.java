package mchorse.mappet.api.scripts.user.items;

import mchorse.mappet.api.scripts.user.nbt.INBTCompound;

/**
 * This interface represents an item stack
 */
public interface IScriptItemStack
{
    /**
     * Whether this item is empty
     */
    public boolean isEmpty();

    /**
     * Get item stack's item
     */
    public IScriptItem item();

    /**
     * Get item stack's count
     */
    public int count();

    /**
     * Get item stack's meta
     */
    public int meta();

    /**
     * Check whether an item stack has an NBT compound tag
     */
    public boolean hasData();

    /**
     * Get item stack's NBT compound tag
     */
    public INBTCompound data();

    /**
     * Replace item stack's NBT compound tag
     */
    public void data(INBTCompound tag);

    /**
     * Serialize item stack to an NBT compound
     */
    public INBTCompound serialize();
}