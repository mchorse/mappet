package mchorse.mappet.api.scripts.user.blocks;

import net.minecraft.block.state.IBlockState;

/**
 * Scripted block state.
 *
 * <p>This interface represents a block state that can be used
 * to compare or place into the world.</p>
 */
public interface IScriptBlockState
{
    /**
     * Get Minecraft block state instance. <b>BEWARE:</b> you need to know the MCP
     * mappings in order to directly call methods on this instance!
     */
    public IBlockState getMinecraftBlockState();

    /**
     * Get block's ID like "minecraft:stone"
     */
    public String getBlockId();

    /**
     * Get meta value of this state (it will always be between 0 and 15)
     */
    public int getMeta();

    /**
     * Check whether given block state contains same block state
     */
    public boolean isSame(IScriptBlockState state);

    /**
     * Check whether given block state has the same block, but
     * not necessarily the same meta value
     */
    public boolean isSameBlock(IScriptBlockState state);
}