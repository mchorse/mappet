package mchorse.mappet.api.scripts.user.entities;

import mchorse.mappet.entities.EntityNpc;

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
     * Sets NPC's tick trigger (Use this if you want to edit an existing `on tick trigger`).
     *
     * <pre>{@code
     *    c.getSubject().setOnTickTrigger("ScriptName", "FunctionName", 1, 0);
     * }</pre>
     *
     * @param scriptName Script name
     * @param functionName Function name
     * @param frequency Frequency
     * @param blockNumber Block number
     */
    public void setOnTickTrigger(String scriptName, String functionName, int frequency, int blockNumber);


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
     * @param blockNumber Block number
     */
    public void setOnInteractTrigger(String scriptName, String functionName, int blockNumber);

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
     */
    public void setPatrol(int x, int y, int z, String scriptName, String functionName);

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

    /**
     * Removes all NPC's patrol points.
     *
     * <pre>{@code
     * c.getSubject().clearPatrolPoints();
     * }</pre>
     */
    public void clearPatrolPoints();

    /**
     * Returns the faction of the npc as a string
     *
     * <pre>{@code
     * c.send(c.getSubject().getFaction())
     * }</pre>
     */
    public String getFaction();
}