package mchorse.mappet.api.scripts.user;

import mchorse.mappet.api.scripts.user.data.ScriptVector;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.mappet.IMappetStates;
import net.minecraft.entity.Entity;

/**
 * Entity interface.
 *
 * This interface represents an entity, it could be a player, NPC,
 * or any other entity.
 */
public interface IScriptEntity
{
    /**
     * Get Minecraft entity instance. <b>BEWARE:</b> you need to know the MCP
     * mappings in order to directly call methods on this instance!
     */
    public Entity getMinecraftEntity();

    /* Entity properties */

    /**
     * Get entity's position
     */
    public ScriptVector getPosition();

    /**
     * Set entity's position (teleport)
     */
    public void setPosition(double x, double y, double z);

    /**
     * Get entity's motion
     */
    public ScriptVector getMotion();

    /**
     * Set entity's motion
     */
    public void setMotion(double x, double y, double z);

    /**
     * Get health points of this entity (20 is the max default for players)
     */
    public float getHp();

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
    public IScriptItemStack getMainItem();

    /**
     * Get item held in off hand
     */
    public IScriptItemStack getOffItem();

    /* Entity meta */

    /**
     * Get unique ID of this entity, which can be used, if needed, in
     * commands as a target selector
     */
    public String getUniqueId();

    /**
     * Get entity's resource location ID, like "minecraft:pig" or
     * "minecraft:zombie"
     */
    public String getEntityId();

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

    /* Mappet stuff */

    /**
     * Get states of this entity (if it has some, only players have states)
     *
     * @return states of a player, or null if an entity doesn't have states
     */
    public IMappetStates states();
}