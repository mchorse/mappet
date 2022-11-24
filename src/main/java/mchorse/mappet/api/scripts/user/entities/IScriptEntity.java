package mchorse.mappet.api.scripts.user.entities;

import mchorse.mappet.api.scripts.user.IScriptFactory;
import mchorse.mappet.api.scripts.user.IScriptRayTrace;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.mappet.api.scripts.user.data.ScriptVector;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.mappet.IMappetStates;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;

/**
 * Entity interface.
 *
 * <p>This interface represents an entity, it could be a player, NPC,
 * or any other entity. <b>IMPORTANT</b>: any method that marks an argument or return
 * as of {@link IScriptEntity} type can return also {@link IScriptPlayer} if it's an
 * actual player, or {@link IScriptNpc} if it's a Mappet NPC!</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        if (c.getSubject().isPlayer())
 *        {
 *            // Do something with the player...
 *        }
 *        if (c.getSubject().isNpc())
 *        {
 *            // Do something with the NPC...
 *        }
 *        else
 *        {
 *            // Do something with the entity...
 *        }
 *    }
 * }</pre>
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
     * Get entity's position.
     *
     * <pre>{@code
     *    var pos = c.getSubject().getPosition();
     *
     *    c.send(c.getSubject().getName() + "'s position is (" + pos.x + ", " + pos.y + ", " + pos.z + ")");
     * }</pre>
     */
    public ScriptVector getPosition();

    /**
     * Set entity's position (teleport).
     *
     * <pre>{@code
     *    c.getSubject().setPosition(800, 8, -135);
     * }</pre>
     */
    public void setPosition(double x, double y, double z);

    /**
     * Get entity's motion.
     *
     * <pre>{@code
     *    var motion = c.getSubject().getMotion();
     *
     *    c.send(c.getSubject().getName() + "'s motion is (" + motion.x + ", " + motion.y + ", " + motion.z + ")");
     * }</pre>
     */
    public ScriptVector getMotion();

    /**
     * Set entity's motion.
     *
     * <pre>{@code
     *    var motion = c.getSubject().getMotion();
     *
     *    if (motion.y < 0)
     *    {
     *        // Reverse the falling motion into a jumping up motion
     *        c.getSubject().setMotion(motion.x, -motion.y, motion.z);
     *    }
     * }</pre>
     */
    public void setMotion(double x, double y, double z);

    /**
     * Get entity's rotation (x is pitch, y is yaw, and z is yaw head, if entity
     * is living base).
     *
     * <pre>{@code
     *    var rotations = c.getSubject().getRotations();
     *    var pitch = rotations.x;
     *    var yaw = rotations.y;
     *    var yaw_head = rotations.z;
     *
     *    c.send(c.getSubject().getName() + "'s rotations are (" + pitch + ", " + yaw + ", " + yaw_head + ")");
     * }</pre>
     */
    public ScriptVector getRotations();

    /**
     * Set entity's rotation.
     *
     * <pre>{@code
     *    // Make entity look at west
     *    c.getSubject().setRotations(0, 0, 0);
     * }</pre>
     */
    public void setRotations(float pitch, float yaw, float yawHead);

    /**
     * Get entity's pitch (vertical rotation).
     */
    public float getPitch();

    /**
     * Get entity's yaw (horizontal rotation).
     */
    public float getYaw();

    /**
     * Get entity's head yaw.
     */
    public float getYawHead();

    /**
     * Get a vector in which direction entity looks.
     *
     * <pre>{@code
     *    var look = c.getSubject().getLook();
     *
     *    c.getSubject().setMotion(look.x * 0.5, look.y * 0.5, look.z * 0.5);
     * }</pre>
     */
    public ScriptVector getLook();

    /**
     * Get entity's current hitbox width (and depth, it's the same number).
     */
    public float getWidth();

    /**
     * Get entity's current hitbox height.
     */
    public float getHeight();

    /**
     * Get health points of this entity (20 is the max default for players).
     *
     * <pre>{@code
     *    var subject = c.getSubject();
     *
     *    if (subject.getHp() < 10)
     *    {
     *        subject.send("Man, you need to replenish your health!");
     *    }
     * }</pre>
     */
    public float getHp();

    /**
     * Set entity's health points. Given value that is more than max HP will get limited to max HP.
     *
     * <pre>{@code
     *    // If entity's health goes below 5 hearts, restore to max
     *    var subject = c.getSubject();
     *
     *    if (subject.getHp() < 10)
     *    {
     *        subject.setHp(subject.getMaxHp());
     *    }
     * }</pre>
     */
    public void setHp(float hp);

    /**
     * Get maximum health points this entity can have.
     *
     * <pre>{@code
     *    var subject = c.getSubject();
     *
     *    subject.send(subject.getName() + " can have up to " + subject.getMaxHp() + " HP!);
     * }</pre>
     */
    public float getMaxHp();

    /**
     * Check whether this entity is on fire.
     *
     * <pre>{@code
     *    var subject = c.getSubject();
     *
     *    // Extinguish the entity if it's on fire
     *    if (subject.isBurning())
     *    {
     *        subject.setBurning(0);
     *    }
     * }</pre>
     */
    public boolean isBurning();

    /**
     * Set entity on fire for given amount of ticks. If <code>0</code> will be
     * provided as the sole argument, then entity's fire will be extinguished.
     *
     * <pre>{@code
     *    var subject = c.getSubject();
     *    var ray = subject.rayTrace(32);
     *
     *    if (ray.isEntity())
     *    {
     *        ray.getEntity().setBurning(2);
     *    }
     * }</pre>
     */
    public void setBurning(int seconds);

    /**
     * Is this entity is sneaking.
     *
     * <pre>{@code
     *    var subject = c.getSubject();
     *
     *    if (subject.isSneaking())
     *    {
     *        subject.send("You completed Simon's task!");
     *    }
     * }</pre>
     */
    public boolean isSneaking();

    /**
     * Is this entity is sprinting.
     *
     * <pre>{@code
     *    var subject = c.getSubject();
     *
     *    if (subject.isSprinting())
     *    {
     *        subject.send("This way, you'll run away way faster from zombies!");
     *    }
     * }</pre>
     */
    public boolean isSprinting();

    /**
     * Is this entity on the ground.
     */
    public boolean isOnGround();

    /* Ray tracing */

    /**
     * Ray trace from entity's looking direction (including any entity intersection).
     * Check {@link IScriptRayTrace} for an example.
     */
    public IScriptRayTrace rayTrace(double maxDistance);

    /**
     * Ray trace from entity's looking direction (excluding entities).
     * Check {@link IScriptRayTrace} for an example.
     */
    public IScriptRayTrace rayTraceBlock(double maxDistance);

    /* Items */

    /**
     * Get item held in main hand.
     *
     * <pre>{@code
     *    var subject = c.getSubject();
     *    var item = subject.getMainItem();
     *
     *    // Lightning bolt admin stick idk I didn't play on servers
     *    if (item.getItem().getId() === "minecraft:stick")
     *    {
     *        c.executeCommand("/summon lightning_bolt ~ ~ ~");
     *    }
     * }</pre>
     */
    public IScriptItemStack getMainItem();

    /**
     * Set item held in main hand.
     *
     * <pre>{@code
     *    // We did a little bit of trolling
     *    c.getSubject().setMainItem(mappet.createItem("minecraft:diamond_hoe"));
     * }</pre>
     */
    public void setMainItem(IScriptItemStack stack);

    /**
     * Get item held in off hand.
     *
     * <pre>{@code
     *    var subject = c.getSubject();
     *    var item = subject.getOffItem();
     *
     *    // Lightning bolt admin stick (but in off hand) idk I didn't play on servers
     *    if (item.getItem().getId() === "minecraft:stick")
     *    {
     *        c.executeCommand("/summon lightning_bolt ~ ~ ~");
     *    }
     * }</pre>
     */
    public IScriptItemStack getOffItem();

    /**
     * Set item held in off hand.
     *
     * <pre>{@code
     *    c.getSubject().setMainItem(mappet.createItem("minecraft:shield"));
     * }</pre>
     */
    public void setOffItem(IScriptItemStack stack);

    /* Entity meta */

    /**
     * Set entity's speed.
     */
    public void setSpeed(float speed);

    /**
     * Get this entity's attack target.
     */
    public IScriptEntity getTarget();

    /**
     * Set this entity's attack target to given entity.
     */
    public void setTarget(IScriptEntity entity);

    /**
     * Check whether entity's AI is enabled.
     */
    public boolean isAIEnabled();

    /**
     * Set entity's AI to be enabled or disabled (if it has it).
     */
    public void setAIEnabled(boolean enabled);

    /**
     * Get unique ID of this entity, which can be used, if needed, in
     * commands as a target selector.
     */
    public String getUniqueId();

    /**
     * Get entity's resource location ID, like "minecraft:pig" or
     * <code>minecraft:zombie</code>.
     */
    public String getEntityId();

    /**
     * Get how many ticks did this entity existed.
     */
    public int getTicks();

    /**
     * Get combined light value of where the entity is currently standing.
     * In order to get torch and sky light values separately, see the example below
     * to "unpack" the combined value.
     *
     * <pre>{@code
     *    var light = c.getPlayer().getCombinedLight();
     *    var skyLight = light / 65536 / 15;
     *    var torchLight = light % 65536 / 15;
     *
     *    // Do something with skyLight and torchLight
     * }</pre>
     */
    public int getCombinedLight();

    /**
     * Get entity name.
     */
    public String getName();

    /**
     * Get entity's full (copy of its) NBT data.
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
     * Check whether this entity is a player.
     */
    public boolean isPlayer();

    /**
     * Check whether this entity is an NPC.
     */
    public boolean isNpc();

    /**
     * Check whether this entity is living base.
     */
    public boolean isLivingBase();

    /**
     * Check whether this entity is same as given entity.
     */
    public boolean isSame(IScriptEntity entity);

    /**
     * Inflict some damage on this entity (use {@link #kill()} to kill the entity though).
     */
    public void damage(float health);

    /**
     * Damage this entity as given entity was the source of attack.
     */
    public void damageAs(IScriptEntity entity, float health);

    /**
     * Damage this entity as given player was the source of the attack with its equipment.
     */
    public void damageWithItemsAs(IScriptPlayer player);

    /**
     * Get entity's fall distance.
     */
    public float getFallDistance();

    /**
     * Set entity's fall distance.
     *
     * <p>You can use this method, by calling it with <code>0</code>, to prevent fall
     * damage when teleporting an entity which was already falling.</p>
     */
    public void setFallDistance(float distance);

    /**
     * Remove this entity from the server without any dead effects (essentially despawn).
     */
    public void remove();

    /**
     * Kill this entity from the server by inflicting lots of damage
     * (similar to <code>/kill</code> command).
     */
    public void kill();

    /**
     * Swing entity's main hand.
     */
    public default void swingArm()
    {
        this.swingArm(0);
    }

    /**
     * Swing entity's arm.
     *
     * @param arm <code>0</code> is primary (main), <code>1</code> is secondary (off-hand).
     */
    public void swingArm(int arm);

    /* Potion effects */

    /**
     * Apply given potion effect on the entity for given duration, with given amplifier,
     * and optionally with particles.
     *
     * <p><b>Attention</b>: potion effects work only with living base entities, so check for
     * {@link #isLivingBase()}! You can get the potion effect using
     * {@link IScriptFactory#getPotion(String)}.</p>
     *
     * <pre>{@code
     *    var slowness = mappet.getPotion("slowness");
     *    var subject = c.getSubject();
     *
     *    subject.applyPotion(slowness, 200, 1, false);
     * }</pre>
     *
     * @param potion Potion that should be applied.
     * @param duration Duration of the effect in ticks (<code>20</code> ticks = <code>1</code> second).
     * @param amplifier How strong is potion effect.
     * @param particles Whether potion effect's particles should be emitted.
     */
    public void applyPotion(Potion potion, int duration, int amplifier, boolean particles);

    /**
     * Check whether given potion effect is present on this entity.
     *
     * <p><b>Attention</b>: potion effects work only with living base entities, so check for
     * {@link #isLivingBase()}! You can get the potion effect using
     * {@link IScriptFactory#getPotion(String)}.</p>
     *
     * <pre>{@code
     *    var slowness = mappet.getPotion("slowness");
     *    var subject = c.getSubject();
     *
     *    if (subject.hasPotion(slowness))
     *    {
     *        subject.send("You're kind of slow, my dude...");
     *    }
     * }</pre>
     */
    public boolean hasPotion(Potion potion);

    /**
     * Remove given potion effect from this entity.
     *
     * <p><b>Attention</b>: potion effects work only with living base entities, so check for
     * {@link #isLivingBase()}! You can get the potion effect using
     * {@link IScriptFactory#getPotion(String)}.</p>
     *
     * <pre>{@code
     *    var slowness = mappet.getPotion("slowness");
     *    var subject = c.getSubject();
     *
     *    if (subject.removePotion(slowness))
     *    {
     *        subject.send("I made you faster, no need to thank me ;)");
     *    }
     * }</pre>
     *
     * @return <code>true</code> if there was effect, and it was successfully removed,
     *         <code>false</code> if had no given effect present.
     */
    public boolean removePotion(Potion potion);

    /**
     * Remove all potion effects from this entity.
     *
     * <p><b>Attention</b>: potion effects work only with living base entities, so check for
     * {@link #isLivingBase()}! You can get the potion effect using
     * {@link IScriptFactory#getPotion(String)}.</p>
     *
     * <pre>{@code
     *    var subject = c.getSubject();
     *
     *    subject.clearPotions();
     *    subject.send("You've been freed from all potion effects!");
     * }</pre>
     */
    public void clearPotions();

    /* Mappet stuff */

    /**
     * Get entity's states (if it has some, only players and NPCs have states).
     *
     * @return entity's states, or null if this entity doesn't have states.
     */
    public IMappetStates getStates();

    /**
     * Get entity's morph (works with player and NPCs).
     *
     * <pre>{@code
     *    // Assuming s is a player
     *    var s = c.getSubject();
     *    var morph = mappet.createMorph("{Name:\"blockbuster.alex\"}");
     *    var entityMorph = s.getMorph();
     *
     *    if (entityMorph != null && entityMorph.equals(morph))
     *    {
     *        c.send(s.getName() + " is morphed into Alex morph!");
     *    }
     * }</pre>
     */
    public AbstractMorph getMorph();

    /**
     * Set entity's morph (works with player and NPCs).
     *
     * <pre>{@code
     *    var morph = mappet.createMorph("{Name:\"blockbuster.alex\"}");
     *
     *    // Assuming c.getSubject() is a player or an NPC
     *    c.getSubject().setMorph(morph);
     * }</pre>
     *
     * @return if entity's morph was changed successfully.
     */
    public boolean setMorph(AbstractMorph morph);

    /**
     * Display a world morph to all players that see this entity (including themselves).
     */
    public default void displayMorph(AbstractMorph morph, int expiration, double x, double y, double z)
    {
        this.displayMorph(morph, expiration, x, y, z, true);
    }

    /**
     * Display a world morph to all players that see this entity (including themselves) with
     * toggleable following rotation.
     *
     * <pre>{@code
     *    var s = c.getSubject();
     *    var morph = mappet.createMorph('{Name:"item"}');
     *
     *    s.displayMorph(morph, 100, 0, s.getHeight() + 0.5, 0, true);
     * }</pre>
     *
     * @param morph Morph that will be displayed (if <code>null</code>, then it won't send anything).
     * @param expiration For how many ticks will this displayed morph exist on the client side.
     * @param rotate Whether attached world morph should replicate entity's rotation (i.e. copy head rotation).
     */
    public default void displayMorph(AbstractMorph morph, int expiration, double x, double y, double z, boolean rotate)
    {
        this.displayMorph(morph, expiration, x, y, z, 0, 0, rotate);
    }

    /**
     * Display a world morph to all players that see this entity (including themselves)
     * toggleable following rotation and rotation offsets.
     *
     * <pre>{@code
     *    var s = c.getSubject();
     *    var morph = mappet.createMorph('{Name:"item"}');
     *
     *    s.displayMorph(morph, 100, 0, s.getHeight() + 0.5, 0, 180, 0, true);
     * }</pre>
     *
     * @param morph Morph that will be displayed (if <code>null</code>, then it won't send anything).
     * @param expiration For how many ticks will this displayed morph exist on the client side.
     * @param yaw Horizontal rotation in degrees.
     * @param pitch Vertical rotation in degrees.
     * @param rotate Whether attached world morph should replicate entity's rotation (i.e. copy head rotation).
     */
    public void displayMorph(AbstractMorph morph, int expiration, double x, double y, double z, float yaw, float pitch, boolean rotate);

    /**
     * Get entity's world.
     * <pre>{@code
     *    var s = c.getSubject();
     *    var world = s.getWorld();
     *
     *    world.setRaining(true);
     * }</pre>
     */
    public IScriptWorld getWorld();
}