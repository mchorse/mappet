package mchorse.mappet.api.scripts.user;

import java.util.Map;

/**
 * Script event.
 *
 * This interface represent the event, the only argument that was passed
 * into script's function. It contains many different useful methods to
 * interact with Minecraft on the server side.
 */
public interface IScriptEvent
{
    /**
     * Get subject (primary) entity that was passed into the event
     */
    public IScriptEntity subject();

    /**
     * Get object (secondary) entity that was passed into the event
     */
    public IScriptEntity object();

    /**
     * Get the world in which this event happened
     */
    public IScriptWorld world();

    /**
     * Get the server in which this event happened
     */
    public IScriptServer server();

    /**
     * Get a map of extra context values that was passed into the event
     */
    public Map<String, Object> values();

    /* Useful methods */

    /**
     * Schedule execution of the same script (with same function)
     * given ticks forward
     */
    public void scheduleScript(int delay);

    /**
     * Schedule execution of the same script with given function
     * given ticks forward
     */
    public void scheduleScript(String function, int delay);

    /**
     * Schedule execution of given script with specific function
     * given ticks forward
     */
    public void scheduleScript(String script, String function, int delay);

    /**
     * Execute a command
     */
    public void executeCommand(String command);

    /**
     * Send a message to all players in the chat
     */
    public void send(String message);

    /**
     * Send a message only to one player
     *
     * @return whether event was able to send the message (i.e. if entity
     *         isn't a player, it will return false)
     */
    public boolean sendTo(IScriptEntity entity, String message);
}