package mchorse.mappet.api.scripts.user.items;

import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;
import net.minecraft.inventory.IInventory;

/**
 * This interface represents an inventory.
 *
 * <p>See {@link IScriptPlayer#getInventory()} and
 * {@link IScriptWorld#getInventory(int, int, int)} for ways to get
 * inventories from player and world, respectively.</p>
 *
 * <pre>{@code
 *    fun main(c: IScriptEvent) {
 *        // Get player's inventory
 *        val subject : IScriptEntity = c.getSubject();
 *        val player : IScriptPlayer? = subject as? IScriptPlayer;
 *
 *        if (subject.isPlayer())
 *        {
 *            val inventory : IScriptInventory = player?.getInventory();
 *
 *            // Do something with player's inventory
 *        }
 *
 *        // Get chest's inventory in the world
 *        val world : IScriptWorld = c.getWorld();
 *        if (world.hasInventory(214, 4, 512))
 *        {
 *            val inventory : IScriptInventory = world.getInventory(214, 4, 512);
 *
 *            // Do something with chest's inventory
 *        }
 *    }
 * }</pre>
 */
public interface IScriptInventory
{
    /**
     * Get Minecraft inventory instance. <b>BEWARE:</b> you need to know the MCP
     * mappings in order to directly call methods on this instance!
     */
    public IInventory getMinecraftInventory();

    /**
     * Check whether this inventory is empty.
     */
    public boolean isEmpty();

    /**
     * Return the maximum amount of item stacks in this inventory.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     // Assuming that c.getSubject() is a player
     *     val subject : IScriptEntity = c.getSubject();
     *     val player : IScriptPlayer? = subject as? IScriptPlayer;
     *     val inventory : IScriptInventory? = player?.getInventory();
     *     val item : IScriptItemStack? = mappet.createItem("minecraft:stick");
     *
     *     if (inventory != null && item != null) {
     *         for (i in 0 until inventory.size()) {
     *             // We do a little bit of trolling
     *             inventory.setStack(i, item);
     *         }
     *     }
     * }
     * }</pre>
     */
    public int size();

    /**
     * Get stack in slot at given index.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     // Assuming that c.getSubject() is a player
     *     val subject : IScriptEntity = c.getSubject();
     *     val player : IScriptPlayer? = subject as? IScriptPlayer;
     *     val inventory : IScriptInventory? = player?.getInventory();
     *
     *     if (inventory != null) {
     *         val first : IScriptItemStack = inventory.getStack(0);
     *
     *         if (first.isEmpty()) {
     *             // Give a stick into first player's hotbar slot
     *             inventory.setStack(0, mappet.createItem("minecraft:stick"));
     *         }
     *     }
     * }
     * }</pre>
     *
     * @return an item stack at given index
     */
    public IScriptItemStack getStack(int index);

    /**
     * Remove a stack at given index
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     // Assuming that c.getSubject() is a player
     *     val subject : IScriptEntity = c.getSubject();
     *     val player : IScriptPlayer? = subject as? IScriptPlayer;
     *     val inventory : IScriptInventory? = player?.getInventory();
     *
     *     if (inventory != null) {
     *         val first : IScriptItemStack = inventory.removeStack(0);
     *
     *         if (first.isEmpty()) {
     *             player?.send("Oh... you had nothing...");
     *         }
     *         else {
     *             player?.send("Ha-ha, I stole your " + first.getItem().getId());
     *         }
     *     }
     * }
     * }</pre>
     *
     * @return removed item stack
     */
    public IScriptItemStack removeStack(int index);

    /**
     * Replace given stack at index.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     // Assuming that c.getSubject() is a player
     *     val subject : IScriptEntity = c.getSubject();
     *     val player : IScriptPlayer? = subject as? IScriptPlayer;
     *     val inventory : IScriptInventory? = player?.getInventory();
     *
     *     if (inventory != null) {
     *         inventory.setStack(4, mappet.createItem("minecraft:diamond_sword"));
     *     }
     * }
     * }</pre>
     */
    public void setStack(int index, IScriptItemStack stack);

    /**
     * Empty the inventory.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     // Assuming that c.getSubject() is a player
     *     val subject : IScriptEntity = c.getSubject();
     *     val player : IScriptPlayer? = subject as? IScriptPlayer;
     *     player?.getInventory()?.clear();
     * }
     * }</pre>
     */
    public void clear();

    /* Basic inventory */

    /**
     * Get basic inventory's name. This works only for inventories that support
     * naming, like chests.
     */
    public String getName();

    /**
     * Whether this inventory has a name. This works only for inventories that
     * support naming, like chests.
     */
    public boolean hasCustomName();

    /**
     * Set basic inventory's name. This works only for inventories that
     * support naming, like chests.
     */
    public void setName(String name);
}