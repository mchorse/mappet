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
     * Set numeric value to existing state by ID
     */
    public void setNumber(String id, double value);

    /**
     * Set string value to existing state by ID
     */
    public void setString(String id, String value);

    /**
     * Get a numeric value of a state by given ID
     *
     * @return state value, or 0 if no state found
     */
    public double getNumber(String id);

    /**
     * Get a string value of a state by given ID
     *
     * @return state value, or empty string if no state found
     */
    public String getString(String id);

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