package mchorse.mappet.api.scripts.user.mappet.triggers.triggerBlocks;

import mchorse.mappet.api.triggers.blocks.CommandTriggerBlock;

/**
 * This section covers Mappet's command trigger blocks.
 */
public interface IMappetTriggerBlockCommand
{
    public CommandTriggerBlock getMinecraftScriptTriggerBlock();

    /**
     * Set the command to be executed
     *
     * <pre>{@code
     * // This example adds a command trigger block to the given region block's `on enter trigger`
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var regionBlockTrigger = regionBlock.getOnEnterTrigger();
     * var commandTriggerBlock = regionBlockTrigger.addCommandBlock();
     * commandTriggerBlock.set("/say @s Welcome to this region!");
     * }</pre>
     *
     * @param command the command to be executed
     */
    public void set(String command);
}
