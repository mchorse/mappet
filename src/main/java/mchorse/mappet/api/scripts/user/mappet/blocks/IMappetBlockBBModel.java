package mchorse.mappet.api.scripts.user.mappet.blocks;

import mchorse.mappet.api.scripts.code.mappet.blocks.MappetBlockBBModel;
import mchorse.mappet.api.scripts.code.mappet.blocks.MappetModelSettings;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.metamorph.api.morphs.AbstractMorph;

/**
 * Blockbuster model block API.
 *
 * <pre>{@code
 * function main(c)
 * {
 *     var bbBlock = mappet.createBBBlock();
 *
 *     var bbBlockSettings = bbBlock.getSettings()
 *         .setGlobalEnabled(true)
 *         .setShadowEnabled(false)
 *         .scale(1, 0.1, 1);
 *
 *     var morph = mappet.createMorph("{Meta:5b,Block:\"minecraft:wool\",Name:\"block\"}");
 *     bbBlock.setMorph(morph);
 *
 *     bbBlock.place(c.getWorld(), 0, 4, 0);
 * }
 * }</pre>
 */
public interface IMappetBlockBBModel {

    /**
     * Place this Blockbuster model block in the world.
     *
     * <pre>{@code
     * var bbBlock = mappet.createBBModelBlock();
     * // do something with the BB block...
     * bbBlock.place(c.getWorld(), -127, 88, 129);
     * }</pre>
     *
     * @param world world to place the block in
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @return this Blockbuster model block instance
     */
    MappetBlockBBModel place(IScriptWorld world, int x, int y, int z);

    /**
     * Sets the morph to this Blockbuster model block.
     *
     * <pre>{@code
     * var bbBlock = mappet.createBBModelBlock();
     * var morph = mappet.createMorph("{Meta:5b,Block:\"minecraft:wool\",Name:\"block\"}");
     * bbBlock.setMorph(morph);
     * bbBlock.place(c.getWorld(), -127, 88, 129)
     * }</pre>
     *
     * @param morph morph to set
     * @return this Blockbuster model block instance
     */
    MappetBlockBBModel setMorph(AbstractMorph morph);

    /**
     * Gets the morph of this Blockbuster model block.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var bbModelBlock = c.getWorld().getBBModelBlock(-127, 75, 183);
     *
     *     var fred = mappet.createMorph('{Settings:{Hands:1b},Name:"blockbuster.fred"}');
     *
     *     var bbModelBlockMorphNBTCompound = bbModelBlock.getMorph().toNBT();
     *     var fredMorphNBTCompound =fred.toNBT();
     *
     *     if (bbModelBlock != null && bbModelBlockMorphNBTCompound.equals(fredMorphNBTCompound))
     *     {
     *         c.send("The model block has indeed Fred's morph!");
     *     }
     * }
     * }</pre>
     *
     * @return the current morph of the Blockbuster model block
     */
    AbstractMorph getMorph();

    /**
     * Clears the morph of this Blockbuster model block.
     *
     * <pre>{@code
     * var bbBlock = c.getWorld().getBBModelBlock(0, 4, 0);
     * bbBlock.clearMorph();
     * }</pre>
     *
     * @return this Blockbuster model block instance
     */
    MappetBlockBBModel clearMorph();

    /**
     * Get Blockbuster model block settings.
     *
     * <pre>{@code
     * var bbBlock = c.getWorld().getBBModelBlock(0, 4, 0);
     * var bbBlockSettings = bbBlock.getSettings()
     *     .setGlobalEnabled(true)
     *     .setShadowEnabled(false)
     *     .scale(1, 0.1, 1);
     * }</pre>
     *
     * @return Blockbuster model block settings
     */
    MappetModelSettings getSettings();
}
