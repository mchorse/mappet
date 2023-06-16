package mchorse.mappet.api.scripts.user.mappet.conditions;

import mchorse.mappet.api.scripts.user.nbt.INBTCompound;

/**
 * This section represents condition's blocks.
 * Here you can get or set the general things of a given condition block.
 */
public interface IMappetConditionBlock
{
    /**
     * Get the type of this condition block
     *
     * <pre>{@code
     * // This example removes all expression condition trigger blocks
     * // from the given region block's condition
     *
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * regionBlock.getCondition().getAllBlocks().forEach(function(conditionBlock) {
     *     var conditionType = conditionBlock.getType();
     *     if (conditionType === "expression") {
     *         conditionBlock.remove();
     *     }
     * });
     * }</pre>
     *
     * @return The type of this condition block
     */
    String getType();

    /**
     * Remove this condition block from the condition
     *
     * <pre>{@code
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     *
     * regionBlock.getCondition().getAllBlocks().forEach(function(block) {
     *     block.remove();
     * });
     * }</pre>
     *
     * @return Whether the block was removed or not
     */
    boolean remove();

    /**
     * Get the NBT representation of this condition block
     *
     * <pre>{@code
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var conditionBlock = regionBlock.getCondition().getAllBlocks()[0];
     * var nbt = conditionBlock.toNBT();
     *
     * // This will print the NBT of the first condition block
     * c.send(nbt.stringify());
     * }</pre>
     *
     * @return The NBT representation of this condition block
     */
    INBTCompound toNBT();
}
