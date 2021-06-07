package mchorse.mappet.api.scripts.user.items;

import net.minecraft.item.Item;

/**
 * This interface represents an item
 */
public interface IScriptItem
{
    /**
     * Get Minecraft item instance. <b>BEWARE:</b> you need to know the MCP
     * mappings in order to directly call methods on this instance!
     */
    public Item getMinecraftItem();

    /**
     * Get item's ID like "minecraft:stick" or "minecraft:diamond_hoe"
     */
    public String getId();

    /**
     * Check whether given item is same as this one
     */
    public boolean isSame(IScriptItem item);
}