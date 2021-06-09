package mchorse.mappet.api.scripts.user;

import mchorse.mappet.api.scripts.user.mappet.IMappetStates;
import net.minecraft.server.MinecraftServer;

import java.util.List;

/**
 * This interface represent the server passed in the event.
 */
public interface IScriptServer
{
    /**
     * Get Minecraft server instance. <b>BEWARE:</b> you need to know the MCP
     * mappings in order to directly call methods on this instance!
     */
    public MinecraftServer getMinecraftServer();

    /**
     * Get all entities matching giving target selector
     */
    public List<IScriptEntity> getEntities(String targetSelector);

    /**
     * Get all players on the server
     */
    public List<IScriptEntity> getAllPlayers();

    /**
     * Get global (server) states
     */
    public IMappetStates getStates();
}