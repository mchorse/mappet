package mchorse.mappet.api.scripts.user.entities;

import mchorse.mappet.api.scripts.code.mappet.triggers.MappetTrigger;
import mchorse.mappet.api.scripts.user.data.ScriptVector;
import mchorse.mappet.entities.EntityNpc;

import java.util.List;

/**
 * Mappet's NPC entity interface.
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        if (c.getSubject().isNpc())
 *        {
 *            // Do something with the NPC...
 *        }
 *    }
 * }</pre>
 */
public interface IScriptNpc extends IScriptEntity
{
    /**
     * Get Mappet entity NPC instance. <b>BEWARE:</b> you need to know the
     * MCP mappings in order to directly call methods on this instance!
     *
     * <p>But some methods might have human readable method names. Please
     * check <a href="https://github.com/mchorse/mappet/blob/master/src/main/java/mchorse/mappet/entities/EntityNpc.java">EntityNpc</a> class for methods that
     * don't have {@link Override} annotation!</p>
     */
    public EntityNpc getMappetNpc();

    /**
     * Get NPC's NPC ID.
     *
     * <pre>{@code
     *    var pos = c.getSubject().getPosition();
     *    var npc = c.getWorld().spawnNpc("test", pos.x, pos.y + 2, pos.z);
     *
     *    // This will output "true" as long as you have an NPC configured
     *    // in Mappet's NPC dashboard panel
     *    c.send(npc.getNpcId() === "test");
     * }</pre>
     */
    public String getNpcId();

    /**
     * Get NPC's state.
     *
     * <pre>{@code
     *    c.send(c.getSubject().getNpcState());
     * }</pre>
     */
    public String getNpcState();

    /**
     * Set NPC's state.
     *
     * <pre>{@code
     *    c.getSubject().setNpcState("default");
     * }</pre>
     *
     * @param stateId state ID
     */
    public void setNpcState(String stateId);

    /**
     * Make NPC can pick up stuff.
     *
     * <pre>{@code
     *    c.getSubject().canPickUpLoot(true);
     * }</pre>
     *
     * @param canPickUpLoot true if NPC can pick up stuff, false otherwise
     */
    public void canPickUpLoot(boolean canPickUpLoot);


    /**
     * Make NPC follow a target.
     *
     * <pre>{@code
     *    c.getSubject().follow("@r");
     * }</pre>
     *
     * @param target Target to follow (can be a player name, @r)
     */
    public void follow(String target);

    /**
     * Sets NPC's patrol point with a script trigger.
     *
     * <pre>{@code
     *    c.getSubject().setPatrol(x, y, z, "ScriptName", "FunctionName", 0);
     * }</pre>
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param scriptName Script name
     * @param functionName Function name
     * @param patrolIndex Patrol index
     */
    public void setPatrol(int x, int y, int z, String scriptName, String functionName, int patrolIndex);

    /**
     * Adds a new patrol point with its trigger to the NPC.
     *
     * <pre>{@code
     * var npc = c.getSubject();
     * var pos = npc.getPosition();
     * var trigger = mappet.createTrigger();
     * trigger.addCommandBlock().set("say I arrived!");
     * npc.addPatrolPoint(pos.x, pos.y, pos.z, trigger);
     * }</pre>
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param trigger trigger to be executed when NPC arrives at the point
     */
    public void addPatrolPoint(int x, int y, int z, MappetTrigger trigger);

    /**
     * Removes a patrol point at a certain indext from the NPC.
     *
     * <pre>{@code
     * c.getSubject().removePatrolPoint(0);
     * }</pre>
     *
     * @param index index of the patrol point to be removed
     */
    public void removePatrolPoint(int index);

    /**
     * Removes a patrol point at a certain position from the NPC.
     *
     * <pre>{@code
     * var npc = c.getSubject();
     * var pos = npc.getPosition();
     * npc.removePatrolPoint(pos.x, pos.y, pos.z);
     * }</pre>
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     */
    public void removePatrolPoint(int x, int y, int z);

    /**
     * Removes all NPC's patrol points.
     *
     * <pre>{@code
     * c.getSubject().clearPatrolPoints();
     * }</pre>
     */
    public void clearPatrolPoints();

    /**
     * Returns the faction of the NPC as a string
     *
     * <pre>{@code
     * c.send(c.getSubject().getFaction())
     * }</pre>
     */
    public String getFaction();

    /**
     * Sets whether the NPC can be steered.
     *
     * <pre>{@code
     * c.getSubject().setCanBeSteered(true);
     * }</pre>
     */
    public void setCanBeSteered(boolean enabled);

    /**
     * Checks if the NPC can be steered.
     *
     * <pre>{@code
     * c.getSubject().canBeSteered();
     * }</pre>
     */
    public boolean canBeSteered();

    /**
     * Sets the steering offset for the NPC.
     *
     * <pre>{@code
     * c.getSubject().setSteeringOffset(index, x, y, z);
     * }</pre>
     */
    public void setSteeringOffset(int index, float x, float y, float z);

    /**
     * Gets the steering offset of the NPC.
     *
     * <pre>{@code
     * c.getSubject().addSteeringOffset(x, y, z);
     * }</pre>
     */
    public void addSteeringOffset(float x, float y, float z);

    /**
     * Gets the steering offset of the NPC.
     *
     * <pre>{@code
     * c.getSubject().getSteeringOffsets().forEach((offset) -> {
     *     c.send(offset.x + ", " + offset.y + ", " + offset.z);
     * });
     * }</pre>
     */
    public List<ScriptVector> getSteeringOffsets();

    /**
     * Sets the speed of the NPC.
     *
     * <pre>{@code
     * c.getSubject().setNpcSpeed(speed);
     * }</pre>
     */
    public void setNpcSpeed(float speed);

    /**
     * Gets the speed of the NPC.
     *
     * <pre>{@code
     * c.getSubject().getNpcSpeed();
     * }</pre>
     */
    public float getNpcSpeed();

    /**
     * Sets the jump power of the NPC.
     *
     * <pre>{@code
     * c.getSubject().setJumpPower(jumpHeight);
     * }</pre>
     */
    public void setJumpPower(float jumpHeight);

    /**
     * Gets the jump power of the NPC.
     *
     * <pre>{@code
     * c.getSubject().getjumpPower();
     * }</pre>
     */
    public float getJumpPower();

    /**
     * Sets whether the NPC is invincible.
     *
     * <pre>{@code
     * c.getSubject().setInvincible(true);
     * }</pre>
     */
    public void setInvincible(boolean invencible);

    /**
     * Checks if the NPC is invincible.
     *
     * <pre>{@code
     * c.getSubject().isInvincible();
     * }</pre>
     */
    public boolean isInvincible();

    /**
     * Sets whether the NPC can swim.
     *
     * <pre>{@code
     * c.getSubject().setCanSwim(true);
     * }</pre>
     */
    public void setCanSwim(boolean canSwim);

    /**
     * Checks if the NPC can swim.
     *
     * <pre>{@code
     * c.getSubject().canSwim();
     * }</pre>
     */
    public boolean canSwim();

    /**
     * Sets whether the NPC is immovable.
     *
     * <pre>{@code
     * c.getSubject().setImmovable(true);
     * }</pre>
     */
    public void setImmovable(boolean immovable);

    /**
     * Checks if the NPC is immovable.
     *
     * <pre>{@code
     * c.getSubject().isImmovable();
     * }</pre>
     */
    public boolean isImmovable();

    /**
     * Sets the shadow size of the NPC.
     *
     * <pre>{@code
     * c.getSubject().setShadowSize(0.8);
     * }</pre>
     *
     * @param size the new shadow size for the NPC.
     */
    public void setShadowSize(float size);

    /**
     * Gets the shadow size of the NPC.
     *
     * <pre>{@code
     * var size = c.getSubject().getShadowSize();
     * }</pre>
     *
     * @return the shadow size of the NPC.
     */
    public float getShadowSize();

    /**
     * Sets the XP value of the NPC.
     *
     * <pre>{@code
     * c.getSubject().setXpValue(10);
     * }</pre>
     *
     * @param xp the new XP value for the NPC.
     * @return the new XP value.
     */
    public float setXpValue(int xp);

    /**
     * Gets the XP value of the NPC.
     *
     * <pre>{@code
     * var xp = c.getSubject().getXpValue();
     * }</pre>
     *
     * @return the XP value of the NPC.
     */
    public int getXpValue();

    /**
     * Gets the path distance of the NPC. Also determines the NPC's sight radius of "look at player" option.
     *
     * <pre>{@code
     * var distance = c.getSubject().getPathDistance();
     * }</pre>
     *
     * @return the path distance of the NPC.
     */
    public float getPathDistance();

    /**
     * Sets the path distance of the NPC. Also determines the NPC's sight radius of "look at player" option.
     *
     * <pre>{@code
     * c.getSubject().setPathDistance(10);
     * }</pre>
     *
     * @param sightRadius the new path distance for the NPC.
     */
    public void setPathDistance(float sightRadius);

    /**
     * Sets the attack range of the NPC.
     *
     * <pre>{@code
     * c.getSubject().setAttackRange(5);
     * }</pre>
     *
     * @param sightDistance the new attack range for the NPC.
     */
    public void setAttackRange(float sightDistance);

    /**
     * Gets the attack range of the NPC.
     *
     * <pre>{@code
     * var range = c.getSubject().getAttackRange();
     * }</pre>
     *
     * @return the attack range of the NPC.
     */
    public float getAttackRange();

    /**
     * Sets the killable status of the NPC.
     * If false, then NPCs can be killed only by a command.
     * Regardless of the state, killable allows to make this NPC damaged
     * until 0 health.
     *
     * <pre>{@code
     * c.getSubject().setKillable(true);
     * }</pre>
     *
     * @param killable the new killable status for the NPC.
     */
    public void setKillable(boolean killable);

    /**
     * Gets the killable status of the NPC.
     * If false, then NPCs can be killed only by a command.
     * Regardless of the state, killable allows to make this NPC damaged
     * until 0 health.
     *
     * <pre>{@code
     * var isKillable = c.getSubject().isKillable();
     * }</pre>
     *
     * @return true if the NPC is killable, false otherwise.
     */
    public boolean isKillable();

    /**
     * Gets the burnable status of the NPC.
     *
     * <pre>{@code
     * var canGetBurned = c.getSubject().canGetBurned();
     * }</pre>
     *
     * @return true if the NPC can get burned, false otherwise.
     */
    public boolean canGetBurned();

    /**
     * Sets the burnable status of the NPC.
     *
     * <pre>{@code
     * c.getSubject().canGetBurned(true);
     * }</pre>
     *
     * @param canGetBurned the new burnable status for the NPC.
     */
    public void canGetBurned(boolean canGetBurned);

    /**
     * Gets the status if the NPC can take fall damage.
     *
     * <pre>{@code
     * var canFallDamage = c.getSubject().canFallDamage();
     * }</pre>
     *
     * @return true if the NPC can take fall damage, false otherwise.
     */
    public boolean canFallDamage();

    /**
     * Sets the status if the NPC can take fall damage.
     *
     * <pre>{@code
     * c.getSubject().canFallDamage(true);
     * }</pre>
     *
     * @param canFallDamage the new status for the NPC's fall damage.
     */
    public void canFallDamage(boolean canFallDamage);

    /**
     * Gets the damage strength points of the NPC.
     *
     * <pre>{@code
     * var damage = c.getSubject().getDamage();
     * }</pre>
     *
     * @return the damage of the NPC.
     */
    public float getDamage();

    /**
     * Sets the damage strength points of the NPC.
     *
     * <pre>{@code
     * c.getSubject().setDamage(10);
     * }</pre>
     *
     * @param damage the new damage for the NPC.
     */
    public void setDamage(float damage);

    /**
     * Gets the damage delay of the NPC.
     *
     * <pre>{@code
     * var delay = c.getSubject().getDamageDelay();
     * }</pre>
     *
     * @return the damage delay of the NPC.
     */
    public int getDamageDelay();

    /**
     * Sets the damage delay of the NPC.
     *
     * <pre>{@code
     * c.getSubject().setDamageDelay(5);
     * }</pre>
     *
     * @param damageDelay the new damage delay for the NPC.
     */
    public void setDamageDelay(int damageDelay);

    /**
     * Gets the wandering status of the NPC.
     *
     * <pre>{@code
     * var doesWander = c.getSubject().doesWander();
     * }</pre>
     *
     * @return true if the NPC wanders, false otherwise.
     */
    public boolean doesWander();

    /**
     * Sets the wandering status of the NPC.
     *
     * <pre>{@code
     * c.getSubject().setWander(true);
     * }</pre>
     *
     * @param wander the new wandering status for the NPC.
     */
    public void setWander(boolean wander);

    /**
     * Gets the status of the NPC's idle look around behavior.
     *
     * <pre>{@code
     * var doesLookAround = c.getSubject().doesLookAround();
     * }</pre>
     *
     * @return true if the NPC looks around while idle, false otherwise.
     */
    public boolean doesLookAround();

    /**
     * Sets the status of the NPC's idle look around behavior.
     *
     * <pre>{@code
     * c.getSubject().setLookAround(true);
     * }</pre>
     *
     * @param lookAround the new idle look around status for the NPC.
     */
    public void setLookAround(boolean lookAround);

    /**
     * Gets the status of the NPC's behavior to look at the player.
     *
     * <pre>{@code
     * var doesLookAtPlayer = c.getSubject().doesLookAtPlayer();
     * }</pre>
     *
     * @return true if the NPC looks at the player, false otherwise.
     */
    public boolean doesLookAtPlayer();

    /**
     * Sets the status of the NPC's behavior to look at the player.
     *
     * <pre>{@code
     * c.getSubject().setLookAtPlayer(true);
     * }</pre>
     *
     * @param lookAtPlayer the new status for the NPC's behavior to look at the player.
     */
    public void setLookAtPlayer(boolean lookAtPlayer);

    /* Triggers */

    /**
     * Gets the NPC's initialize trigger for programmatically editing.
     *
     * <pre>{@code
     * var trigger = c.getSubject().getOnInitializeTrigger();
     * var scriptTriggerBlock = regionBlockTrigger.addScriptBlock();
     * scriptTriggerBlock.setInlineCode(function() {
     *     c.send("NPC initialized!");
     * });
     * }</pre>
     *
     * @return the NPC's initialize trigger.
     */
    public MappetTrigger getOnInitializeTrigger();

    /**
     * Gets the NPC's interact trigger for programmatically editing.
     *
     * <pre>{@code
     * var trigger = c.getSubject().getOnInteractTrigger();
     * var scriptTriggerBlock = regionBlockTrigger.addScriptBlock();
     * scriptTriggerBlock.setInlineCode(function() {
     *     c.send("Hello, " + c.getObject().getName() + "!");
     * });
     * }</pre>
     *
     * @return the NPC's interact trigger.
     */
    public MappetTrigger getOnInteractTrigger();

    /**
     * Gets the NPC's damaged trigger for programmatically editing.
     *
     * <pre>{@code
     * var trigger = c.getSubject().getOnDamagedTrigger();
     * var scriptTriggerBlock = trigger.addScriptBlock();
     * scriptTriggerBlock.setInlineCode(function() {
     *     c.send("NPC has been damaged!");
     * });
     * }</pre>
     *
     * @return the NPC's damaged trigger.
     */
    public MappetTrigger getOnDamagedTrigger();

    /**
     * Gets the NPC's death trigger for programmatically editing.
     *
     * <pre>{@code
     * var trigger = c.getSubject().getOnDeathTrigger();
     * var scriptTriggerBlock = trigger.addScriptBlock();
     * scriptTriggerBlock.setInlineCode(function() {
     *     c.send("NPC has died!");
     * });
     * }</pre>
     *
     * @return the NPC's death trigger.
     */
    public MappetTrigger getOnDeathTrigger();

    /**
     * Gets the NPC's tick trigger for programmatically editing.
     *
     * <pre>{@code
     * var trigger = c.getSubject().getOnTickTrigger();
     * var scriptTriggerBlock = trigger.addScriptBlock();
     * scriptTriggerBlock.setInlineCode(function() {
     *     c.send("NPC tick!");
     * });
     * }</pre>
     *
     * @return the NPC's tick trigger.
     */
    public MappetTrigger getOnTickTrigger();

    /**
     * Gets the NPC's target trigger for programmatically editing.
     *
     * <pre>{@code
     * var trigger = c.getSubject().getOnTargetAcquiredTrigger();
     * var scriptTriggerBlock = trigger.addScriptBlock();
     * scriptTriggerBlock.setInlineCode(function() {
     *     c.send("NPC has started targeting!");
     * });
     * }</pre>
     *
     * @return the NPC's target trigger.
     */
    public MappetTrigger getOnTargetTrigger();

    /**
     * Gets the NPC's collision trigger for programmatically editing.
     *
     * <pre>{@code
     * var trigger = c.getSubject().getOnCollisionTrigger();
     * var scriptTriggerBlock = trigger.addScriptBlock();
     * scriptTriggerBlock.setInlineCode(function() {
     *     c.send("NPC has collided with someone!");
     * });
     * }</pre>
     *
     * @return the NPC's collision trigger.
     */
    public MappetTrigger getOnCollisionTrigger();

    /**
     * Gets the NPC's respawn trigger for programmatically editing.
     *
     * <pre>{@code
     * var trigger = c.getSubject().getOnRespawnTrigger();
     * var scriptTriggerBlock = trigger.addScriptBlock();
     * scriptTriggerBlock.setInlineCode(function() {
     *     c.send("NPC has respawned!");
     * });
     * }</pre>
     *
     * @return the NPC's respawn trigger.
     */
    public MappetTrigger getOnRespawnTrigger();


    /* Deprecated */

    /**
     * Sets NPC's tick trigger (Use this if you want to edit an existing `on tick trigger`).
     *
     * <pre>{@code
     *    c.getSubject().setOnTickTrigger("ScriptName", "FunctionName", 1, 0);
     * }</pre>
     *
     * @param scriptName Script name
     * @param functionName Function name
     * @param frequency Frequency
     * @param blockIndex Block number
     */
    public void setOnTickTrigger(String scriptName, String functionName, int frequency, int blockIndex);


    /**
     * Adds a new `on tick trigger` to the NPC.
     *
     * <pre>{@code
     *    c.getSubject().addOnTickTrigger("ScriptName", "FunctionName", 1);
     * }</pre>
     *
     * @param scriptName Script name
     * @param functionName Function name
     * @param frequency Frequency
     */
    public void addOnTickTrigger(String scriptName, String functionName, int frequency);

    /**
     * Removes all `on tick` triggers from the NPC.
     *
     * <pre>{@code
     *    c.getSubject().clearOnTickTriggers();
     * }</pre>
     */
    public void clearOnTickTriggers();

    /**
     * Sets NPC's on interaction trigger.
     *
     * <pre>{@code
     *    c.getSubject().setOnInteractTrigger("ScriptName", "FunctionName", 0);
     * }</pre>
     *
     * @param scriptName Script name
     * @param functionName Function name
     * @param blockIndex Block number
     */
    public void setOnInteractTrigger(String scriptName, String functionName, int blockIndex);

    /**
     * Adds NPC's on interaction trigger.
     *
     * <pre>{@code
     *    c.getSubject().addOnInteractTrigger("ScriptName", "FunctionName");
     * }</pre>
     *
     * @param scriptName Script name
     * @param functionName Function name
     */
    public void addOnInteractTrigger(String scriptName, String functionName);

    /**
     * Clears NPC's on interaction triggers.
     *
     * <pre>{@code
     *    c.getSubject().clearOnInteractTriggers();
     * }</pre>
     */
    public void clearOnInteractTriggers();

    /**
     * Adds a new NPC's patrol point with a script trigger.
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param z Z coordinate
     * @param scriptName Script name
     * @param functionName Function name
     */
    public void addPatrol(int x, int y, int z, String scriptName, String functionName);
}