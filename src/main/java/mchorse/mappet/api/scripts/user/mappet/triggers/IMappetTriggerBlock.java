package mchorse.mappet.api.scripts.user.mappet.triggers;

import mchorse.mappet.api.scripts.user.nbt.INBTCompound;

/**
 * This section represents trigger's blocks.
 * Here you can get or set the general things of a given trigger block.
 */
public interface IMappetTriggerBlock
{
    /**
     * Get the type of this trigger block
     *
     * <pre>{@code
     * // This example removes all command trigger blocks from the given
     * // region block's `on enter trigger`
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var regionBlockTrigger = regionBlock.getOnEnterTrigger();
     * var allTriggerBlocks = regionBlockTrigger.getAllBlocks();
     * for each (var triggerBlock in allTriggerBlocks) {
     *     var triggerType = triggerBlock.getType();
     *     if (triggerType === "command") {
     *         triggerBlock.remove();
     *     }
     * }
     * }</pre>
     *
     * @return the type of this trigger block
     */
    String getType();

    /**
     * Get the frequency of this trigger block
     *
     * <pre>{@code
     * // This example sets the frequency of all command trigger blocks
     * // from the given region block's `on tick trigger` to 20
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var regionBlockTrigger = regionBlock.getOnTickTrigger();
     * var allTriggerBlocks = regionBlockTrigger.getAllBlocks();
     * for each (var triggerBlock in allTriggerBlocks) {
     *     var triggerType = triggerBlock.getType();
     *     if (triggerType === "command") {
     *         triggerBlock.setFrequency(20);
     *     }
     * }
     * }</pre>
     *
     */
    void setFrequency(int frequency);

    /**
     * Get the frequency of this trigger block
     *
     * <pre>{@code
     * // This example sends in chat the frequency of all command trigger
     * // blocks from the given region block's `on tick trigger`
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var regionBlockTrigger = regionBlock.getOnTickTrigger();
     * var allTriggerBlocks = regionBlockTrigger.getAllBlocks();
     * for each (var triggerBlock in allTriggerBlocks) {
     *     var triggerType = triggerBlock.getType();
     *     if (triggerType === "command") {
     *         var frequency = triggerBlock.getFrequency();
     *         c.send(frequency);
     *     }
     * }
     * }</pre>
     *
     * @return the frequency of this trigger block
     */
    int getFrequency();

    /**
     * Remove this trigger block
     *
     * <pre>{@code
     * // This example removes all command trigger blocks from the
     * // given region block's `on enter trigger`
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var regionBlockTrigger = regionBlock.getOnEnterTrigger();
     * var allTriggerBlocks = regionBlockTrigger.getAllBlocks();
     * for each (var triggerBlock in allTriggerBlocks) {
     *     var triggerType = triggerBlock.getType();
     *     if (triggerType === "command") {
     *         triggerBlock.remove();
     *     }
     * }
     * }</pre>
     *
     * @return true if the trigger block was removed, false otherwise
     */
    boolean remove();

    /**
     * Get the NBT representation of this trigger block
     *
     * <pre>{@code
     * // This example logs all NBT representations of all command trigger
     * // blocks from the given region block's `on tick trigger`
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var regionBlockTrigger = regionBlock.getOnTickTrigger();
     * var allTriggerBlocks = regionBlockTrigger.getAllBlocks();
     * for each (var triggerBlock in allTriggerBlocks) {
     *     var triggerType = triggerBlock.getType();
     *     if (triggerType === "command") {
     *         var nbt = triggerBlock.getNBT();
     *         print(nbt);
     *     }
     * }
     * }</pre>
     *
     * @return the NBT representation of this trigger block
     */
    INBTCompound toNBT();
}
