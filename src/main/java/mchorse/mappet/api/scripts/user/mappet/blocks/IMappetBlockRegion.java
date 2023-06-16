package mchorse.mappet.api.scripts.user.mappet.blocks;

import mchorse.mappet.api.scripts.code.mappet.triggers.MappetTrigger;
import mchorse.mappet.api.scripts.user.IScriptWorld;

/**
 * Represents the interface for a programmatically manipulated region block.
 *
 * <pre>{@code
 * function main(c)
 * {
 *     var regionBlock = mappet.createRegionBlock()
 *         .setPassable(true)
 *         .setCheckEntities(true)
 *         .setUpdateFrequency(1)
 *
 *         .addCylinderShape(0.75, 0.5, 0, 1, 0);
 *
 *     var regionBlockTrigger = regionBlock.getOnEnterTrigger()
 *         .addScriptBlock()
 *         .setInlineCode(function() {
 *             c.getSubject().send("Welcome to this region!");
 *         });
 *
 *     regionBlock.place(c.getWorld(), 0, 4, 0);
 * }
 * }</pre>
 */
public interface IMappetBlockRegion
{
    /**
     * Sets whether the region block is passable.
     *
     * <pre>{@code
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * regionBlock.setPassable(true);
     * }</pre>
     *
     * @param isPassable whether the region block is passable
     * @return this
     */
    IMappetBlockRegion setPassable(boolean isPassable);

    /**
     * Places the region block at the given coordinates.
     *
     * <pre>{@code
     * var regionBlock = mappet.createRegionBlock();
     * //do something with the region block...
     * regionBlock.place(c.getWorld(), 0, 4, 0);
     * }</pre>
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @return this
     */
    IMappetBlockRegion place(IScriptWorld world, int x, int y, int z);

    /**
     * Sets whether the region block should check entities.
     *
     * <pre>{@code
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * regionBlock.setCheckEntities(true);
     * }</pre>
     * @param checkEntities whether the region block should check entities
     * @return this
     */
    IMappetBlockRegion setCheckEntities(boolean checkEntities);

    /**
     * Sets the delay of the region block.
     *
     * <pre>{@code
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * regionBlock.setDelay(20);
     * }</pre>
     * @param delay the delay
     * @return this
     */
    IMappetBlockRegion setDelay(int delay);

    /**
     * Sets the update frequency of the region block.
     *
     * <pre>{@code
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * regionBlock.setUpdateFrequency(1);
     * }</pre>
     *
     * @param frequency the update frequency
     */
    IMappetBlockRegion setUpdateFrequency(int frequency);

    /**
     * Adds a box shape to the region block.
     *
     * <pre>{@code
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * regionBlock.addBoxShape(1, 1, 1, 0, 0, 0);
     * }</pre>
     *
     * @param offsetZ the z offset
     * @param halfSizeX half the size of the x axis
     * @param halfSizeY half the size of the y axis
     * @param halfSizeZ half the size of the z axis
     * @param offsetX the x offset
     * @param offsetY the y offset
     */
    IMappetBlockRegion addBoxShape(double halfSizeX, double halfSizeY, double halfSizeZ, double offsetZ, double offsetX, double offsetY);

    /**
     * Adds a box shape to the region block without an offset.
     *
     * <pre>{@code
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * regionBlock.addBoxShape(1, 1, 1);
     * }</pre>
     *
     * @param halfSizeX half the size of the x axis
     * @param halfSizeY half the size of the y axis
     * @param halfSizeZ half the size of the z axis
     */
    IMappetBlockRegion addBoxShape(double halfSizeX, double halfSizeY, double halfSizeZ);

    /**
     * Adds a sphere shape to the region block without an offset.
     *
     * <pre>{@code
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * regionBlock.addSphereShape(1, 1);
     * }</pre>
     *
     * @param horizontalRadius the horizontal radius of the sphere
     * @param verticalRadius the vertical radius of the sphere
     */
    IMappetBlockRegion addSphereShape(double horizontalRadius, double verticalRadius);

    /**
     * Adds a sphere shape to the region block.
     *
     * <pre>{@code
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * regionBlock.addSphereShape(1, 1, 0, 0, 0);
     * }</pre>
     *
     * @param horizontalRadius the horizontal radius of the sphere
     * @param verticalRadius the vertical radius of the sphere
     * @param offsetZ the z offset
     * @param offsetX the x offset
     * @param offsetY the y offset
     */
    IMappetBlockRegion addSphereShape(double horizontalRadius, double verticalRadius, double offsetZ, double offsetX, double offsetY);

    /**
     * Adds a cylinder shape to the region block.
     *
     * <pre>{@code
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * regionBlock.addCylinderShape(1, 1, 0, 0, 0);
     * }</pre>
     *
     * @param radius the radius of the cylinder
     * @param height the height of the cylinder
     * @param offsetZ the z offset
     * @param offsetX the x offset
     * @param offsetY the y offset
     */
    IMappetBlockRegion addCylinderShape(double radius, double height, double offsetZ, double offsetX, double offsetY);

    /**
     * Adds a cylinder shape to the region block without an offset.
     *
     * <pre>{@code
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * regionBlock.addCylinderShape(1, 1);
     * }</pre>
     *
     * @param radius the radius of the cylinder
     * @param height the height of the cylinder
     */
    IMappetBlockRegion addCylinderShape(double radius, double height);

    /**
     * Gets on enter trigger of the region block.
     *
     * <pre>{@code
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var regionBlockTrigger = regionBlock.getOnEnterTrigger();
     * var scriptTriggerBlock = regionBlockTrigger.addScriptBlock();
     * scriptTriggerBlock.setInlineCode(function() {
     *     c.getSubject().send("You entered the region!");
     * });
     * }</pre>
     *
     * @return the on enter trigger
     */
    MappetTrigger getOnEnterTrigger();

    /**
     * Gets on exit trigger of the region block.
     *
     * <pre>{@code
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var regionBlockTrigger = regionBlock.getOnExitTrigger();
     * var scriptTriggerBlock = regionBlockTrigger.addScriptBlock();
     * scriptTriggerBlock.setInlineCode(function() {
     *     c.getSubject().send("You left the region!");
     * });
     * }</pre>
     *
     * @return the on exit trigger
     */
    MappetTrigger getOnExitTrigger();

    /**
     * Gets on tick trigger of the region block.
     *
     * <pre>{@code
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var regionBlockTrigger = regionBlock.getOnTickTrigger();
     * var scriptTriggerBlock = regionBlockTrigger.addScriptBlock();
     * scriptTriggerBlock.setInlineCode(function() {
     *     c.getSubject().send("You are in the region!");
     * });
     * }</pre>
     *
     * @return the on tick trigger
     */
    MappetTrigger getOnTickTrigger();
}