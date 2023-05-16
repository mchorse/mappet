package mchorse.mappet.api.scripts.user.items;

import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import net.minecraft.item.ItemStack;

import java.util.List;

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
     * Whether this item is empty.
     */
    public boolean isEmpty();

    /**
     * Get item stack's item.
     */
    public IScriptItem getItem();

    /**
     * Get a copy of item stack.
     */
    public IScriptItemStack copy();

    /**
     * Get item stack's maximum size.
     */
    public int getMaxCount();

    /**
     * Get item stack's count.
     */
    public int getCount();

    /**
     * Set item stack's count.
     */
    public void setCount(int count);

    /**
     * Get item stack's meta.
     */
    public int getMeta();

    /**
     * Set item stack's meta.
     */
    public void setMeta(int meta);

    /**
     * Check whether an item stack has an NBT compound tag.
     */
    public boolean hasData();

    /**
     * Get item stack's NBT compound tag.
     */
    public INBTCompound getData();

    /**
     * Replace item stack's NBT compound tag.
     */
    public void setData(INBTCompound tag);

    /**
     * Serialize item stack to an NBT compound.
     */
    public INBTCompound serialize();

    /**
     * Get display name of the item stack.
     */
    public String getDisplayName();

    /**
     * Set display name of the item stack.
     */
    public void setDisplayName(String name);

    /**
     * Get lore of the item stack.
     */
    public String getLore(int index);

    /**
     * Get all lore lines of the item stack as a list.
     */
    public List<String> getLoreList();

    /**
     * Set lore of the item stack.
     */
    public void setLore(int index, String lore);

    /**
     * Add a lore line to the item stack.
     */
    public void addLore(String lore);

    /**
     * Remove all lore lines from the item stack.
     */
    public void clearAllLores();

    /**
     * Remove a lore line from the item stack.
     */
    public void clearLore(int index);

    /**
     * Clear all enchantments from the item stack.
     */
    public void clearAllEnchantments();

    /**
     * Get a list of all blocks the item stack can destroy.
     */
    public List<String> getCanDestroyBlocks();

    /**
     * Add a block that the item stack can destroy.
     */
    public void addCanDestroyBlock(String block);

    /**
     * Clear all blocks that the item stack can destroy.
     */
    public void clearAllCanDestroyBlocks();

    /**
     * Clear a block that the item stack can destroy.
     */
    public void clearCanDestroyBlock(String block);

    /**
     * Get a list of all blocks the item stack can place on.
     */
    public List<String> getCanPlaceOnBlocks();

    /**
     * Add a block that the item stack can place on.
     */
    public void addCanPlaceOnBlock(String block);

    /**
     * Clear all blocks that the item stack can place on.
     */
    public void clearAllCanPlaceOnBlocks();

    /**
     * Clear a block that the item stack can place on.
     */
    public void clearCanPlaceOnBlock(String block);

    /**
     * Get repair cost of the item stack.
     */
    public int getRepairCost();

    /**
     * Set repair cost of the item stack.
     */
    public void setRepairCost(int cost);

    /**
     * Check if an item stack is unbreakable.
     */
    public boolean isUnbreakable();

    /**
     * Set whether an item stack is unbreakable or not.
     */
    public void setUnbreakable(boolean unbreakable);

    /**
     * Add/remove more items to the stack.
     */
    public void add(int amount);
}