package mchorse.mappet.api.scripts.user.blocks;

import net.minecraft.block.state.IBlockState;

/**
 * Scripted block state.
 *
 * <p>This interface represents a block state that can be used
 * to compare or place into the world. You can use {@link mchorse.mappet.api.scripts.user.IScriptFactory#createBlockState(String, int)}
 * to query for comparison.</p>
 *
 * <pre>{@code
 *    var andesite = mappet.createBlockState("minecraft:stone", 5);
 *
 *    function main(c)
 *    {
 *        if (c.getWorld().getBlock(214, 3, 511).isSame(andesite))
 *        {
 *            c.getSubject().send("Block at (214, 3, 511) is indeed andesite!");
 *        }
 *    }
 * }</pre>
 */
public interface IScriptBlockState
{
    /**
     * Get Minecraft block state instance. <b>BEWARE:</b> you need to know the MCP
     * mappings in order to directly call methods on this instance!
     */
    public IBlockState getMinecraftBlockState();

    /**
     * Get block's ID like <code>minecraft:stone</code>.
     *
     * <pre>{@code
     *    var block = c.getWorld().getBlock(214, 3, 511);
     *
     *    c.getSubject().send("Block at (214, 3, 511) is " + block.getBlockId());
     * }</pre>
     */
    public String getBlockId();

    /**
     * Get meta value of this state (it will always be between 0 and 15).
     *
     * <pre>{@code
     *    var andesite = mappet.createBlockState("minecraft:stone", 5);
     *
     *    // This will print "Andesite's meta is 5"
     *    c.getSubject().send("Andesite's meta is " + andesite.getMeta());
     * }</pre>
     */
    public int getMeta();

    /**
     * Check whether this block state is same as given block state.
     *
     * <pre>{@code
     *    var andesite = mappet.createBlockState("minecraft:stone", 5);
     *
     *    if (c.getWorld().getBlock(214, 3, 511).isSame(andesite))
     *    {
     *        c.getSubject().send("Block at (214, 3, 511) is indeed andesite!");
     *    }
     * }</pre>
     */
    public boolean isSame(IScriptBlockState state);

    /**
     * Check whether given block state has the same block, but
     * not necessarily the same meta value.
     *
     * <pre>{@code
     *    var andesite = mappet.createBlockState("minecraft:stone", 5);
     *    var stone = mappet.createBlockState("minecraft:stone", 0);
     *
     *    // This will print true
     *    c.getSubject().send(stone.isSameBlock(andesite));
     * }</pre>
     */
    public boolean isSameBlock(IScriptBlockState state);

    /**
     * Check whether given block state is air.
     */
    public boolean isAir();
}