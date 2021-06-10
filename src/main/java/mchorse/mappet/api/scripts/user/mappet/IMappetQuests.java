package mchorse.mappet.api.scripts.user.mappet;

import java.util.Set;

/**
 * This interface represents Mappet player's quests
 */
public interface IMappetQuests
{
    /**
     * Check whether these quests have a suqest by given ID
     */
    public boolean has(String id);

    /**
     * Add a quest into these quests by given ID
     *
     * @return true if a quest was successfully added, false if player has already
     *         this quest, or if the quest doesn't exist
     */
    public boolean add(String id);

    /**
     * Check whether a quest by given ID can be completed
     */
    public boolean isComplete(String id);

    /**
     * Complete (and reward) the quest in these quests by given ID
     *
     * @return true if player was rewarded and quest was removed from the quests
     *         list, false if the quest by given ID isn't present
     */
    public boolean complete(String id);

    /**
     * Remove the quest from these quests by given ID
     *
     * @return true if the quest was removed, false if the quest wasn't even
     *         present in these quests
     */
    public boolean decline(String id);

    /**
     * Get all present quests' IDs
     */
    public Set<String> getIds();
}