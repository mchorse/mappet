package mchorse.mappet.api.scripts.user;

import mchorse.mappet.api.scripts.user.blocks.IScriptBlockState;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.api.scripts.user.nbt.INBTList;

/**
 * Scripting API factory that allows to initialize/create different stuff.
 */
public interface IScriptFactory
{
    /**
     * Get a block state that can be used to place and compare blocks in
     * the {@link IScriptWorld}
     */
    public IScriptBlockState blockState(String blockId, int meta);

    /**
     * Create an empty NBT compound
     */
    public default INBTCompound compound()
    {
        return this.compound(null);
    }

    /**
     * Parse an NBT compound data out of given string, if string NBT was
     * invalid then an empty compound will be returned
     */
    public INBTCompound compound(String nbt);

    /**
     * Create an empty NBT list
     */
    public default INBTList list()
    {
        return this.list(null);
    }

    /**
     * Parse an NBT list data out of given string, if string NBT was
     * invalid then an empty list will be returned
     */
    public INBTList list(String nbt);

    /**
     * Create an item stack out of string NBT
     */
    public default IScriptItemStack itemStack(String nbt)
    {
        return this.itemStack(this.compound(nbt));
    }

    /**
     * Create an item stack out of string NBT
     */
    public IScriptItemStack itemStack(INBTCompound compound);
}