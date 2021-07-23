package mchorse.mappet.api.scripts.user.entities;

import mchorse.mappet.api.scripts.user.items.IScriptInventory;
import mchorse.mappet.api.scripts.user.mappet.IMappetQuests;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Player entity interface.
 *
 * <p>This interface represents a player entity.</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        if (c.getSubject().isPlayer())
 *        {
 *            // Do something with the player...
 *        }
 *    }
 * }</pre>
 */
public interface IScriptPlayer extends IScriptEntity
{
    /**
     * Get Minecraft player entity instance. <b>BEWARE:</b> you need to know the
     * MCP mappings in order to directly call methods on this instance!
     */
    public EntityPlayerMP getMinecraftPlayer();

    /**
     * Get player's game mode.
     *
     * <pre>{@code
     *    // Assuming that c.getSubject() is a player
     *    var player = c.getSubject();
     *    var gamemode = player.getGameMode();
     *
     *    if (gamemode === 0)
     *    {
     *        player.send("You're in survival mode!");
     *    }
     * }</pre>
     *
     * @return Player's game mode as an integer, <code>0</code> is survival, <code>1</code>
     * is creative, <code>2</code> is adventure , and <code>3</code> is spectator.
     */
    public int getGameMode();

    /**
     * Set player's game mode.
     *
     * <pre>{@code
     *    // Assuming that c.getSubject() is a player
     *    var player = c.getSubject();
     *    var gamemode = c.getSubject().getGameMode();
     *
     *    // When player exits the mining region, set their game mode back to adventure
     *    if (gamemode === 0 && !player.getStates().has("region.mining_factory"))
     *    {
     *        player.setGameMode(2);
     *    }
     * }</pre>
     *
     * @param gameMode Player's game mode <code>0</code> is survival, <code>1</code>
     * is creative, <code>2</code> is adventure , and <code>3</code> is spectator.
     */
    public void setGameMode(int gameMode);

    /**
     * Get player's inventory.
     *
     * <pre>{@code
     *    // Assuming that c.getSubject() is a player
     *    var inventory = c.getSubject().getInventory();
     *    var item = mappet.createItem("minecraft:diamond_sword");
     *
     *    // This will change the first slot in the hotbar to a diamond sword
     *    inventory.setStack(0, item);
     * }</pre>
     */
    public IScriptInventory getInventory();

    /**
     * Get player's ender chest inventory.
     *
     * <pre>{@code
     *    // Assuming that c.getSubject() is a player
     *    var inventory = c.getSubject().getEnderChest();
     *    var item = mappet.createItem("minecraft:diamond_sword");
     *
     *    // This will change the first slot in player's ender chest to a diamond sword
     *    inventory.setStack(0, item);
     * }</pre>
     */
    public IScriptInventory getEnderChest();

    /**
     * Send a message to this entity.
     *
     * <pre>{@code
     *    // Assuming that c.getSubject() is a player
     *    c.send("I love all my players equally.");
     *    c.getSubject().send("...but between you and me, you're my favorite player ;)");
     * }</pre>
     */
    public void send(String message);

    /* Mappet stuff */

    /**
     * Get entity's quests (if it has some, only players have quests).
     *
     * <pre>{@code
     *    // Assuming that c.getSubject() is a player
     *    var quests = c.getSubject().getQuests();
     *
     *    if (!quests.has("important_quest"))
     *    {
     *        c.getSubject().send("I think you should complete the main quest chain before attempting side quests...");
     *    }
     * }</pre>
     */
    public IMappetQuests getQuests();
}