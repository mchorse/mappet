package mchorse.mappet.api.scripts.user;

import mchorse.mappet.api.scripts.user.mappet.IMappetStates;

import java.util.List;

/**
 * This interface represent the server passed in the event.
 */
public interface IScriptServer
{
    /**
     * Get all entities matching giving target selector
     */
    public List<IScriptEntity> query(String targetSelector);

    /**
     * Get all players on the server
     */
    public List<IScriptEntity> players();

    /**
     * Get global (server) states
     */
    public IMappetStates states();
}