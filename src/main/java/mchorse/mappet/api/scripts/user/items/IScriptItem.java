package mchorse.mappet.api.scripts.user.items;

/**
 * This interface represents an item
 */
public interface IScriptItem
{
    /**
     * Get item's ID like "minecraft:stick" or "minecraft:diamond_hoe"
     */
    public String id();

    /**
     * Check whether given item is same as this one
     */
    public boolean isSame(IScriptItem item);
}