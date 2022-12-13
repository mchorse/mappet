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
     * Check if a player is online.
     *
     * <pre>{@code
     *    if (!c.getServer().isOnline("McHorse"))
     *    {
     *        c.send("McHorse is not online. :(")
     *    }
     * }</pre>
     */
    public boolean isOnline(String username);

    /**
     * Check if a player is in certain coordinates.
     *
     * <pre>{@code
     * function main(c) {
     *     var player_name = c.getSubject().getName();
     *     if (c.getServer().testForPlayer(player_name, x,y,z))
     *     {
     *         c.send("Subject is not standing on the block.")
     *     }
     * }
     * }</pre>
     */
    public boolean testForPlayer(String username, int x, int y, int z);

    /**
     * Check if a player is in certain coordinates.
     *
     * <pre>{@code
     * function main(c) {
     *     var player_name = c.getSubject().getName();
     *     if (c.getServer().testForPlayer("player_name", x1,y1,z1, x2,y2,z2))
     *     {
     *         c.send("Subject is in range! :o")
     *     }
     * }
     * }</pre>
     */
    public boolean testForPlayer(String username, int x1, int y1, int z1, int x2, int y2, int z2);
}