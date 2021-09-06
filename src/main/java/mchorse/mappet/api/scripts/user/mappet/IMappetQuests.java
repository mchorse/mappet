package mchorse.mappet.api.scripts.user.mappet;

import java.util.Set;

/**
 * This interface represents Mappet player's quests.
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        // Assuming that subject entity is a IScriptPlayer
 *        var quests = c.getSubject().getQuests();
 *
 *        // Do something with quests...
 *    }
 * }</pre>
 */
public interface IMappetQuests
{
    /**
     * Check whether these quests have a quest by given ID.
     *
     * <pre>{@code
     *    if (c.getSubject().getQuests().has("important_quest"))
     *    {
     *        // \u00A7 is section symbol
     *        c.getSubject().send("You can't do this until you finish \u00A76Important quest\u00A7r!");
     *    }
     * }</pre>
     */
    public boolean has(String id);

    /**
     * Add a quest into these quests by given ID.
     *
     * <pre>{@code
     *    if (c.getSubject().getQuests().add("important_quest"))
     *    {
     *        c.getSubject().send("Check your quests!");
     *    }
     *    else
     *    {
     *        c.getSubject().send("You already have this quest, huh...");
     *    }
     * }</pre>
     *
     * @return <code>true</code> if a quest was successfully added, <code>false</code> if
     *         player has already this quest, or if the quest doesn't exist.
     */
    public boolean add(String id);

    /**
     * Check whether a quest by given ID can be completed.
     *
     * <pre>{@code
     *    if (c.getSubject().getQuests().isComplete("important_quest"))
     *    {
     *        c.getSubject().send("I think you should bring this quest back to Steve!");
     *    }
     * }</pre>
     */
    public boolean isComplete(String id);

    /**
     * Complete (and reward) the quest in these quests by given ID.
     *
     * <pre>{@code
     *    if (c.getSubject().getQuests().complete("important_quest"))
     *    {
     *        c.getSubject().send("Important quest was successfully completed!");
     *    }
     *    else
     *    {
     *        c.getSubject().send("Finish your objectives first...");
     *    }
     * }</pre>
     *
     * @return <code>true</code> if player was rewarded and quest was removed from the
     *         quests list, <code>false</code> if the quest by given ID isn't present.
     */
    public boolean complete(String id);

    /**
     * Remove the quest from these quests by given ID.
     *
     * <pre>{@code
     *    if (c.getSubject().getQuests().decline("important_quest"))
     *    {
     *        c.getSubject().send("You failed the objective... you'll need to retake quest  !");
     *    }
     * }</pre>
     *
     * @return <code>true</code> if the quest was removed, <code>false</code> if the
     *         quest wasn't even present in these quests.
     */
    public boolean decline(String id);

    /**
     * Get all present quests' IDs.
     *
     * <pre>{@code
     *    var quests = c.getSubject().getQuests();
     *    var ids = quests.getIds();
     *    var completedAll = true;
     *
     *    for each (var id in ids)
     *    {
     *        if (!quests.isComplete(id))
     *        {
     *            completedAll = false;
     *
     *            break;
     *        }
     *    }
     *
     *    if (completedAll)
     *    {
     *        c.getSubject().send("You need to complete all before you can do this...");
     *    }
     * }</pre>
     */
    public Set<String> getIds();
}