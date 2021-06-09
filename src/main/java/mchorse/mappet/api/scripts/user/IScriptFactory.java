package mchorse.mappet.api.scripts.user;

import mchorse.mappet.api.scripts.user.blocks.IScriptBlockState;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.api.scripts.user.nbt.INBTList;
import net.minecraft.util.EnumParticleTypes;

/**
 * Scripting API factory that allows to initialize/create different stuff.
 */
public interface IScriptFactory
{
    /**
     * Get a block state that can be used to place and compare blocks in
     * the {@link IScriptWorld}
     */
    public IScriptBlockState createBlockState(String blockId, int meta);

    /**
     * Create an empty NBT compound
     */
    public default INBTCompound createCompound()
    {
        return this.createCompound(null);
    }

    /**
     * Parse an NBT compound data out of given string, if string NBT was
     * invalid then an empty compound will be returned
     */
    public INBTCompound createCompound(String nbt);

    /**
     * Create an empty NBT list
     */
    public default INBTList createList()
    {
        return this.createList(null);
    }

    /**
     * Parse an NBT list data out of given string, if string NBT was
     * invalid then an empty list will be returned
     */
    public INBTList createList(String nbt);

    /**
     * Create an item stack out of string NBT
     */
    public default IScriptItemStack createItemStack(String nbt)
    {
        return this.createItemStack(this.createCompound(nbt));
    }

    /**
     * Create an item stack out of string NBT
     */
    public IScriptItemStack createItemStack(INBTCompound compound);

    /**
     * Get Minecraft particle type by its name
     */
    public EnumParticleTypes getParticleType(String type);
}