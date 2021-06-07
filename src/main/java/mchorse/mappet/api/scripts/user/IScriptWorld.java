package mchorse.mappet.api.scripts.user;

import mchorse.mappet.api.scripts.user.blocks.IScriptBlockState;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

/**
 * This interface represent a world passed in the event.
 */
public interface IScriptWorld
{
    /**
     * Get Minecraft world instance. <b>BEWARE:</b> you need to know the MCP
     * mappings in order to directly call methods on this instance!
     */
    public World getMinecraftWorld();

    /**
     * Set a block at XYZ, use {@link IScriptFactory#blockState(String, int)}
     * to get the block state
     */
    public void setBlock(IScriptBlockState state, int x, int y, int z);

    /**
     * Get block state at XYZ
     */
    public IScriptBlockState getBlock(int x, int y, int z);

    /**
     * Check whether it's raining in the world
     */
    public boolean isRaining();

    /**
     * Set raining state
     */
    public void setRaining(boolean raining);

    /**
     * Get current time of day
     */
    public long getTime();

    /**
     * Set current time of day
     */
    public void setTime(long time);

    /**
     * Get total time that this world existed for
     */
    public long getTotalTime();

    /**
     * Get world's dimension ID
     */
    public int getDimensionId();

    /**
     * Spawn vanilla particles
     *
     * @param type Particle type, you can use {@link IScriptFactory#particleType(String)}
     *             to get the desired particle type.
     * @param longDistance Whether particles should be spawned regardless of the distance
     * @param x X coordinate of position where particles should be spawned
     * @param y Y coordinate of position where particles should be spawned
     * @param z Z coordinate of position where particles should be spawned
     * @param n How many particles of given type should be spawned
     * @param dx X random offset that shift particle relative to X coordinate where it spawned
     * @param dy Y random offset that shift particle relative to Y coordinate where it spawned
     * @param dz Z random offset that shift particle relative to Z coordinate where it spawned
     * @param speed The speed of particle, different particles might use this argument differently
     * @param args Additional arguments that can be passed into a particle, for example for
     *             "iconcrack" particle you can pass item numeric ID to spawn item particle for
     *             a specific item (F3 + H shows the numeric ID of an item)
     */
    public void spawnParticles(EnumParticleTypes type, boolean longDistance, double x, double y, double z, int n, double dx, double dy, double dz, double speed, int... args);

    /**
     * Spawn vanilla particles only to a specific player
     *
     * @param player The player that you want to limit seeing the particle only to
     * @param type Particle type, you can use {@link IScriptFactory#particleType(String)}
     *             to get the desired particle type.
     * @param longDistance Whether particles should be spawned regardless of the distance
     * @param x X coordinate of position where particles should be spawned
     * @param y Y coordinate of position where particles should be spawned
     * @param z Z coordinate of position where particles should be spawned
     * @param n How many particles of given type should be spawned
     * @param dx X random offset that shift particle relative to X coordinate where it spawned
     * @param dy Y random offset that shift particle relative to Y coordinate where it spawned
     * @param dz Z random offset that shift particle relative to Z coordinate where it spawned
     * @param speed The speed of particle, different particles might use this argument differently
     * @param args Additional arguments that can be passed into a particle, for example for
     *             "iconcrack" particle you can pass item numeric ID to spawn item particle for
     *             a specific item (F3 + H shows the numeric ID of an item)
     */
    public void spawnParticles(IScriptEntity player, EnumParticleTypes type, boolean longDistance, double x, double y, double z, int n, double dx, double dy, double dz, double speed, int... args);
}