package mchorse.mappet.api.scripts.user.items;

import net.minecraft.inventory.IInventory;

/**
 * This interface represents an inventory
 */
public interface IScriptInventory
{
    /**
     * Get Minecraft inventory instance. <b>BEWARE:</b> you need to know the MCP
     * mappings in order to directly call methods on this instance!
     */
    public IInventory getMinecraftInventory();

    /**
     * Check whether this inventory is empty
     */
    public boolean isEmpty();

    /**
     * Return the maximum amount of item stacks in this inventory
     */
    public int size();

    /**
     * Get stack in slot at given index
     *
     * @return an item stack at given index
     */
    public IScriptItemStack getStack(int index);

    /**
     * Remove a stack at given index
     *
     * @return removed item stack
     */
    public IScriptItemStack removeStack(int index);

    /**
     * Replace given stack at index
     */
    public void setStack(int index, IScriptItemStack stack);

    /* Basic inventory */

    /**
     * Get basic inventory's name
     */
    public String getName();

    /**
     * Whether this inventory has a name
     */
    public boolean hasCustomName();

    /**
     * Set basic inventory's name
     */
    public void setName(String name);
}