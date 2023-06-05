package mchorse.mappet.api.scripts.user.mappet;

import java.util.Set;

/**
 * This interface represents Mappet states. Server ({@link mchorse.mappet.api.scripts.user.IScriptServer}),
 * players ({@link mchorse.mappet.api.scripts.user.entities.IScriptPlayer})
 * and NPCs ({@link mchorse.mappet.api.scripts.user.entities.IScriptNpc}) can have states.
 *
 * <pre>{@code
 *    fun main(c: IScriptEvent) {
 *     // Get global states from the server
 *     val states: IMappetStates = c.getServer().getStates()
 *
 *     // Do something with global states...
 * }
 * }</pre>
 */
public interface IMappetStates
{
    /**
     * Add some value to existing state by ID.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val states: IMappetStates = c.getServer().getStates()
     *
     *     states.add("total_spending", 20.0)
     *     c.send("Total spending is now ${states.getNumber("total_spending")}")
     * }
     * }</pre>
     *
     * @return original value plus the provided value
     */
    public double add(String id, double value);

    /**
     * Set numeric value to existing state by ID.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val states: IMappetStates = c.getServer().getStates()
     *
     *     states.setNumber("total_spending", 1000000001.0)
     *     c.send("Total spending is now ${states.getNumber("total_spending").toString()}")
     * }
     * }</pre>
     */
    public void setNumber(String id, double value);

    /**
     * Set string value to existing state by ID.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val subject : IScriptEntity = c.getSubject();
     *     val subjectStates: IMappetStates? = subject?.getStates()
     *
     *     if (subjectStates==null) return;
     *     subjectStates.setString("name", "Jeff")
     *     (subject as IScriptPlayer)?.send("Your name is now ${subjectStates.getString("name")}")
     * }
     * }</pre>
     */
    public void setString(String id, String value);

    /**
     * Get a numeric value of a state by given ID.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val states: IMappetStates = c.getServer().getStates()
     *
     *     c.send("Total spending is ${states.getNumber("total_spending").toString()}")
     * }
     * }</pre>
     *
     * @return state value, or 0 if no state found
     */
    public double getNumber(String id);

    /**
     * Check if a state instance of number.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val states: IMappetStates = c.getServer().getStates()
     *
     *     c.send("State is number: ${states.isNumber("state_number")}")
     * }
     * }</pre>
     */
    public boolean isNumber(String id);

    /**
     * Get a string value of a state by given ID.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val states: IMappetStates = c.getSubject().getStates()
     *
     *     c.send("Your RPG class is: ${states.getString("rpg_class")}")
     * }
     * }</pre>
     *
     * @return state value, or empty string if no state found
     */
    public String getString(String id);

    /**
     * Check if a state instance of string.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val states: IMappetStates = c.getServer().getStates()
     *
     *     c.send("State is string: ${states.isString("state_string")}")
     * }
     * }</pre>
     */
    public boolean isString(String id);

    /**
     * Removes a state by given ID.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val states: IMappetStates = c.getServer().getStates()
     *
     *     // The city has been defaulted
     *     states.reset("total_spendings")
     * }
     * }</pre>
     */
    public void reset(String id);

    /**
     * Removes multiple states by using mask.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val states: IMappetStates = c.getServer().getStates()
     *
     *     // Remove all states that start with "regions."
     *     states.resetMasked("regions.*")
     * }
     * }</pre>
     */
    public void resetMasked(String id);

    /**
     * Remove all states.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val states: IMappetStates = c.getServer().getStates()
     *
     *     // Game over
     *     states.clear()
     * }
     * }</pre>
     */
    public void clear();

    /**
     * Check whether state by given ID exists.
     *
     * <pre>{@code
     *  fun main(c: IScriptEvent) {
     *     val subject : IScriptEntity = c.getSubject();
     *     val subjectStates: IMappetStates? = subject.getStates()
     *
     *     if (subjectStates==null) return;
     *     if (subjectStates.has("name")) {
     *         (subject as IScriptPlayer).send("Your name is ${subjectStates.getString("name")}")
     *     }
     *     else {
     *         (subject as IScriptPlayer).send("You have no name")
     *     }
     * }
     * }</pre>
     */
    public boolean has(String id);

    /**
     * Get IDs of all states.
     *
     * <pre>{@code
     * fun main(c: IScriptEvent) {
     *     val states: Set<String> = c.getSubject().getStates().keys()
     *
     *     for (key in states) {
     *         c.send("Player states are: " + key)
     *     }
     * }
     * }</pre>
     */
    public Set<String> keys();
}