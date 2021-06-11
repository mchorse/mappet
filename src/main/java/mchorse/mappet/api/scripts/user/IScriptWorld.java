package mchorse.mappet.api.scripts.user;

import mchorse.mappet.api.scripts.user.blocks.IScriptBlockState;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;

import java.util.List;

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
     * Set a block at XYZ, use {@link IScriptFactory#createBlockState(String, int)}
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
     * @param type Particle type, you can use {@link IScriptFactory#getParticleType(String)}
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
     * @param type Particle type, you can use {@link IScriptFactory#getParticleType(String)}
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

    /**
     * Spawn an entity at given position
     */
    public IScriptEntity spawnEntity(String id, double x, double y, double z);

    /**
     * Spawn an entity at given position with additional data
     */
    public IScriptEntity spawnEntity(String id, double x, double y, double z, INBTCompound compound);

    /**
     * Get entities within the box specified by given coordinates in this world.
     * This method limits to scanning entities <b>only within 100 blocks</b>
     * in any direction. If the box provided has any of its sizes that is longer
     * than 100 blocks, then it will simply return an empty list.
     */
    public List<IScriptEntity> getEntities(double x1, double y1, double z1, double x2, double y2, double z2);

    /**
     * Get entities within the sphere specified by given coordinates and radius in
     * this world. This method limits to scanning entities <b>only within 50 blocks
     * radius</b> in any direction. If the sphere provided has the radius that is
     * longer than 100 blocks, then it will simply return an empty list.
     */
    public List<IScriptEntity> getEntities(double x, double y, double z, double radius);

    /**
     * Play a sound event in the world
     */
    public default void playSound(String event, double x, double y, double z)
    {
        this.playSound(event, x, y, z, 1F, 1F);
    }

    /**
     * Play a sound event in the world with volume and pitch
     */
    public void playSound(String event, double x, double y, double z, float volume, float pitch);
}