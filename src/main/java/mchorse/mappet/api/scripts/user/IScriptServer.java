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
 *    fun main(c: IScriptEvent) {
 *        val server : IScriptServer = c.getServer();
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
     * fun main(c: IScriptEvent) {
     *     val overworld : IScriptWorld = c.getServer().getWorld(0)
     *
     *     // Do something with the world...
     * }
     * }</pre>
     */
    public IScriptWorld getWorld(int dimension);

    /**
     * Get fancy world at dimension ID.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val overworld : IScriptFancyWorld = c.getServer().getFancyWorld(0)
     *
     *     // Do something with the world...
     * }
     * }</pre>
     */
    public IScriptFancyWorld getFancyWorld(int dimension);

    /**
     * Get all entities matching giving target selector.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val allCows : List<IScriptEntity> = c.getServer().getEntities("@e[type=minecraft:cow]")
     *
     *     // Despawn all cows
     *     for (cow in allCows) {
     *         cow.remove()
     *     }
     * }
     * }</pre>
     */
    public List<IScriptEntity> getEntities(String targetSelector);

    /**
     * Get an entity by its UUID.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val entity : IScriptEntity = c.getServer().getEntity("02e4ead1-b6ac-42f5-ac8e-a80aa301527b");
     *     val subject : IScriptEntity = c.getSubject();
     *     c.send("Is entity OtakuGamer_? ${entity.isSame(subject)}")
     * }
     * }</pre>
     */
    public IScriptEntity getEntity(String uuid);

    /**
     * Get all players on the server.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val allPlayers : List<IScriptPlayer> = c.getServer().getAllPlayers()
     *
     *     for (player in allPlayers){
     *         player.setMotion(0.0, 0.5, 0.0)
     *     }
     * }
     * }</pre>
     */
    public List<IScriptPlayer> getAllPlayers();

    /**
     * Get a player by their username.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val player : IScriptPlayer = c.getServer().getPlayer("Notch")
     *
     *     if (player != null)
     *     {
     *         // I'm about to pull a prank on Notch... >:)
     *         //
     *         // Or give a nice present... :)
     *     }
     * }
     * }</pre>
     */
    public IScriptPlayer getPlayer(String username);

    /**
     * Check if a player is online.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     if (!c.getServer().isOnline("McHorse"))
     *     {
     *         c.send("McHorse is not online. :(")
     *     }
     * }
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
     * fun main(c: IScriptEvent) {
     *     val states = c.getServer().getStates()
     *
     *     if (states.getNumber("total_money_earned") > 1000000000)
     *     {
     *         // Give all players an achievement or something...
     *     }
     * }
     * }</pre>
     */
    public IMappetStates getStates();

    /**
     * Check if an entity with given UUID exists.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     if (c.getServer().entityExists("29a91933-86f2-4683-8a87-218084d8c927"))
     *     {
     *         // Do something...
     *     }
     * }
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
     * fun main(c: IScriptEvent) {
     *     c.getServer().executeScript("example_script.kts")
     * }
     * }</pre>
     *
     * @param scriptName The name of the script to execute.
     */
    public void executeScript(String scriptName);

    /**
     * Execute a script with a given script name and a specified function.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     c.getServer().executeScript("example_script.kts", "custom_function")
     * }
     * }</pre>
     *
     * @param scriptName The name of the script to execute.
     * @param function   The name of the function within the script to execute.
     */
    public void executeScript(String scriptName, String function);
}