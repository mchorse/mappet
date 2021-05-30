package mchorse.mappet.api.scripts.user;

import java.util.Map;

/**
 * Script event
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
    public IScriptEntity getSubject();

    /**
     * Get object (secondary) entity that was passed into the event
     */
    public IScriptEntity getObject();

    /**
     * Get the world in which this event happened
     */
    public IScriptWorld getWorld();

    /**
     * Get a map of extra context values that was passed into the event
     */
    public Map<String, Object> getValues();

    /* Factory stuff */

    /**
     * Get a block state that can be used to place and compare blocks in
     * the {@link IScriptWorld}
     */
    public IScriptBlockState getBlockState(String blockId, int meta);

    /* Useful methods */

    /**
     * Execute a command
     */
    public void executeCommand(String command);

    /**
     * Send a message to all players in the chat
     */
    public void sendMessage(String message);

    /**
     * Send a message only to one player
     *
     * @return whether event was able to send the message (i.e. if entity
     *         isn't a player, it will return false)
     */
    public boolean sendMessageTo(IScriptEntity entity, String message);
}