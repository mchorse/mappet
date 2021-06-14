package mchorse.mappet.api.scripts.user.entities;

import mchorse.mappet.api.scripts.user.items.IScriptInventory;
import mchorse.mappet.api.scripts.user.mappet.IMappetQuests;

/**
 * Player entity interface.
 *
 * <p>This interface represents a player entity.</p>
 */
public interface IScriptPlayer extends IScriptEntity
{
    /**
     * Get player's inventory
     */
    public IScriptInventory getInventory();

    /**
     * Get player's ender chest inventory
     */
    public IScriptInventory getEnderChest();

    /**
     * Send a message to this entity
     *
     * @return whether it was possible to send the message (i.e. if entity
     *         isn't a player, it will return false)
     */
    public boolean send(String message);

    /* Mappet stuff */

    /**
     * Get entity's quests (if it has some, only players have quests)
     *
     * @return player's quests, or null if this entity doesn't have quests
     */
    public IMappetQuests getQuests();
}