package mchorse.mappet.api.scripts.user;

import mchorse.mappet.api.scripts.user.data.ScriptVector;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;

/**
 * Entity interface.
 *
 * This interface represents an entity, it could be a player, NPC,
 * or any other entity.
 */
public interface IScriptEntity
{
    /* Entity properties */

    /**
     * Get entity's position
     */
    public ScriptVector position();

    /**
     * Set entity's position (teleport)
     */
    public void position(double x, double y, double z);

    /**
     * Get entity's motion
     */
    public ScriptVector motion();

    /**
     * Set entity's motion
     */
    public void motion(double x, double y, double z);

    /**
     * Get health points of this entity (20 is the max default for players)
     */
    public float hp();

    /**
     * Is this entity is sneaking
     */
    public boolean isSneaking();

    /**
     * Is this entity is sprinting
     */
    public boolean isSprinting();

    /* Items */

    /**
     * Get item held in main hand
     */
    public IScriptItemStack mainItem();

    /**
     * Get item held in off hand
     */
    public IScriptItemStack offItem();

    /* Entity meta */

    /**
     * Get unique ID of this entity, which can be used, if needed, in
     * commands as a target selector
     */
    public String uniqueId();

    /**
     * Get entity's resource location ID, like "minecraft:pig" or
     * "minecraft:zombie"
     */
    public String entityId();

    /**
     * Check whether this entity is a player
     */
    public boolean isPlayer();

    /**
     * Remove this entity from the server without any dead effects
     */
    public void remove();

    /**
     * Kill this entity from the server by inflicting lots of damage (similar to /kill command)
     */
    public void kill();
}