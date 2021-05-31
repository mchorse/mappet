package mchorse.mappet.api.scripts.user.mappet;

import java.util.Set;

/**
 * This interface represents Mappet states
 */
public interface IMappetStates
{
    /**
     * Add some value to existing state by ID
     *
     * @return original value plus the provided value
     */
    public double add(String id, double value);

    /**
     * Set some value to existing state by ID
     */
    public void set(String id, double value);

    /**
     * Get a value of a state by given ID
     *
     * @return state value, or 0 if no state found
     */
    public double get(String id);

    /**
     * Removes a state by given ID
     */
    public void reset(String id);

    /**
     * Remove all states
     */
    public void clear();

    /**
     * Check whether state by given ID exists
     */
    public boolean has(String id);

    /**
     * Get IDs of all states
     */
    public Set<String> keys();
}