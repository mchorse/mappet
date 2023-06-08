package mchorse.mappet.api.scripts.user;

import mchorse.mappet.api.scripts.user.entities.IScriptEntity;
import mchorse.mappet.api.scripts.user.entities.IScriptPlayer;
import mchorse.mappet.api.scripts.user.mappet.IMappetStates;
import net.minecraft.server.MinecraftServer;

import java.util.List;

/**
 * This interface represent the server passed in the event.
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        var server = c.getServer();
 *
 *        // Do something with server...
 *    }
 * }</pre>
 */
public interface IScriptServer
{
    /**
     * Get Minecraft server instance. <b>BEWARE:</b> you need to know the MCP
     * mappings in order to directly call methods on this instance!
     */
    public MinecraftServer getMinecraftServer();

    /**
     * Get world at dimension ID.
     *
     * <pre>{@code
     *    var overworld = c.getServer().getWorld(0);
     *
     *    // Do something with the world...
     * }</pre>
     */
    public IScriptWorld getWorld(int dimension);

    /**
     * Get fancy world at dimension ID.
     *
     * <pre>{@code
     *    var overworld = c.getServer().getFancyWorld(0);
     *
     *    // Do something with the world...
     * }</pre>
     */
    public IScriptFancyWorld getFancyWorld(int dimension);

    /**
     * Get all entities matching giving target selector.
     *
     * <pre>{@code
     *    var cows = c.getServer().getEntities("@e[type=minecraft:cow]");
     *
     *    // Despawn all cows
     *    for (var i in cows)
     *    {
     *        cows[i].remove();
     *    }
     * }</pre>
     */
    public List<IScriptEntity> getEntities(String targetSelector);

    /**
     * Get an entity by its UUID.
     *
     * <pre>{@code
     *    var uuid = "29a91933-86f2-4683-8a87-218084d8c927";
     *    var entity = c.getServer().getEntity(uuid);
     *
     *    print(entity.getUniqueId() === uuid); // true
     * }</pre>
     */
    public IScriptEntity getEntity(String uuid);

    /**
     * Get all players on the server.
     *
     * <pre>{@code
     *    var players = c.getServer().getAllPlayers();
     *
     *    for (var i in players)
     *    {
     *        // Surprise :)
     *        players[i].setMotion(0, 0.5, 0);
     *    }
     * }</pre>
     */
    public List<IScriptPlayer> getAllPlayers();

    /**
     * Get a player by their username.
     *
     * <pre>{@code
     *    var player = c.getServer().getPlayer("Notch");
     *
     *    if (player)
     *    {
     *        // I'm about to pull a prank on Notch... >:)
     *        //
     *        // Or give a nice present... :)
     *    }
     * }</pre>
     */
    public IScriptPlayer getPlayer(String username);

    /**
     * Check if a player is online.
     *
     * <pre>{@code
     *    if (!c.getServer().isOnline("McHorse"))
     *    {
     *        c.send("McHorse is not online. :(")
     *    }
     * }</pre>
     */
    public default boolean isOnline(String username)
    {
        return this.getPlayer(username) != null;
    }

    /**
     * Get global (server) states.
     *
     * <pre>{@code
     *    var states = c.getServer().getStates();
     *
     *    if (states.getNumber("total_money_earned") > 1000000000)
     *    {
     *        // Give all players an achievement or something...
     *    }
     * }</pre>
     */
    public IMappetStates getStates();

    /**
     * Check if an entity with given UUID exists.
     *
     * <pre>{@code
     *    if (c.getServer().entityExists("29a91933-86f2-4683-8a87-218084d8c927"))
     *    {
     *        // Do something...
     *    }
     * }</pre>
     *
     * @param uuid The UUID of the entity to check for existence.
     *
     * @return true if an entity with the specified UUID exists, false otherwise.
     */
    public boolean entityExists(String uuid);

    /**
     * Execute a script with a given script name and the default function "main".
     *
     * <pre>{@code
     *    c.getServer().executeScript("example_script.js");
     * }</pre>
     *
     * @param scriptName The name of the script to execute.
     */
    public void executeScript(String scriptName);

    /**
     * Execute a script with a given script name and a specified function.
     *
     * <pre>{@code
     *    c.getServer().executeScript("example_script.js", "custom_function");
     * }</pre>
     *
     * @param scriptName The name of the script to execute.
     * @param function   The name of the function within the script to execute.
     */
    public void executeScript(String scriptName, String function);
}