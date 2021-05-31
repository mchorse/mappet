package mchorse.mappet.api.scripts.user;

/**
 * World
 *
 * This interface represent a world passed in the event
 */
public interface IScriptWorld
{
    /**
     * Set a block at XYZ, use {@link IScriptEvent#blockState(String, int)}
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
}