package mchorse.mappet.api.scripts.user.items;

import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import net.minecraft.item.ItemStack;

/**
 * This interface represents an item stack
 */
public interface IScriptItemStack
{
    /**
     * Get Minecraft item stack instance. <b>BEWARE:</b> you need to know the MCP
     * mappings in order to directly call methods on this instance!
     */
    public ItemStack getMinecraftItemStack();

    /**
     * Whether this item is empty
     */
    public boolean isEmpty();

    /**
     * Get item stack's item
     */
    public IScriptItem getItem();

    /**
     * Get item stack's count
     */
    public int getCount();

    /**
     * Get item stack's meta
     */
    public int getMeta();

    /**
     * Check whether an item stack has an NBT compound tag
     */
    public boolean hasData();

    /**
     * Get item stack's NBT compound tag
     */
    public INBTCompound getData();

    /**
     * Replace item stack's NBT compound tag
     */
    public void setData(INBTCompound tag);

    /**
     * Serialize item stack to an NBT compound
     */
    public INBTCompound serialize();
}