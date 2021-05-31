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
     * Get a map of extra context values that was passed into the event
     */
    public Map<String, Object> values();

    /* Useful methods */

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