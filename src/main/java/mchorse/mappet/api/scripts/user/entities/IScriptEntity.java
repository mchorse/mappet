package mchorse.mappet.api.scripts.user.entities;

import mchorse.mappet.api.scripts.user.IScriptFactory;
import mchorse.mappet.api.scripts.user.IScriptRayTrace;
import mchorse.mappet.api.scripts.user.data.ScriptVector;
import mchorse.mappet.api.scripts.user.items.IScriptInventory;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.mappet.IMappetQuests;
import mchorse.mappet.api.scripts.user.mappet.IMappetStates;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.entity.Entity;

/**
 * Entity interface.
 *
 * <p>This interface represents an entity, it could be a player, NPC,
 * or any other entity. <b>IMPORTANT</b>: any method that marks an argument or return
 * as of {@link IScriptEntity} type can return also {@link IScriptPlayer} if it's an
 * actual player!</p>
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
     * Get entity's rotation (x is pitch, y is yaw, and z is yaw head, if entity
     * is living base)
     */
    public ScriptVector getRotations();

    /**
     * Set entity's rotation
     */
    public void setRotations(float pitch, float yaw, float yawHead);

    /**
     * Get entity's pitch (vertical rotation)
     */
    public float getPitch();

    /**
     * Get entity's yaw (horizontal rotation)
     */
    public float getYaw();

    /**
     * Get entity's head yaw
     */
    public float getYawHead();

    /**
     * Get look vector
     */
    public ScriptVector getLook();

    /**
     * Get health points of this entity (20 is the max default for players)
     */
    public float getHp();

    /**
     * Set entity's health points. Given value that is more than max HP will get limited to max HP.
     */
    public void setHp(float hp);

    /**
     * Get maximum health points this entity can have
     */
    public float getMaxHp();

    /**
     * Is this entity is sneaking
     */
    public boolean isSneaking();

    /**
     * Is this entity is sprinting
     */
    public boolean isSprinting();

    /* Ray tracing */

    /**
     * Ray trace from entity's looking direction (including any entity intersection)
     */
    public IScriptRayTrace rayTrace(double maxDistance);

    /**
     * Ray trace from entity's looking direction (excluding entities)
     */
    public IScriptRayTrace rayTraceBlock(double maxDistance);

    /* Items */

    /**
     * Get item held in main hand
     */
    public IScriptItemStack getMainItem();

    /**
     * Set item held in main hand
     */
    public void setMainItem(IScriptItemStack stack);

    /**
     * Get item held in off hand
     */
    public IScriptItemStack getOffItem();

    /**
     * Set item held in off hand
     */
    public void setOffItem(IScriptItemStack stack);

    /* Entity meta */

    /**
     * Set entity's speed
     */
    public void setSpeed(float speed);

    /**
     * Get this entity's attack target
     */
    public IScriptEntity getTarget();

    /**
     * Set this entity's attack target to given entity
     */
    public void setTarget(IScriptEntity entity);

    /**
     * Check whether entity's AI is enabled
     */
    public boolean isAIEnabled();

    /**
     * Set entity's AI to be enabled or disabled (if it has it)
     */
    public void setAIEnabled(boolean enabled);

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
     * Get how many ticks did this entity existed
     */
    public int getTicks();

    /**
     * Get entity name
     */
    public String getName();

    /**
     * Get entity's full (copy of its) NBT data
     */
    public INBTCompound getFullData();

    /**
     * Overwrite NBT data of this entity. <b>WARNING</b>: use it only if you know
     * what are you doing as this method can corrupt entities.
     */
    public void setFullData(INBTCompound data);

    /**
     * Get Forge's custom tag compound in which you can story any
     * data you want.
     *
     * <p>There is no setter method as you can directly work with returned
     * NBT compound. Any changes to returned compound <b>will be reflected
     * upon entity's data</b>.</p>
     */
    public INBTCompound getEntityData();

    /**
     * Check whether this entity is a player
     */
    public boolean isPlayer();

    /**
     * Check whether this entity is an NPC
     */
    public boolean isNpc();

    /**
     * Check whether this entity is living base
     */
    public boolean isLivingBase();

    /**
     * Inflict some damage on this entity (use {@link #kill()} to kill the entity though)
     */
    public void damage(float health);

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
     * Get entity's states (if it has some, only players and NPCs have states)
     *
     * @return entity's states, or null if this entity doesn't have states
     */
    public IMappetStates getStates();

    /**
     * Set entity's morph (works with player and NPCs). <b>ProTip</b>: use
     * {@link IScriptFactory#createMorph(String)} to create a morph from
     * NBT.
     *
     * @return if entity's morph was changed successfully
     */
    public boolean setMorph(AbstractMorph morph);
}