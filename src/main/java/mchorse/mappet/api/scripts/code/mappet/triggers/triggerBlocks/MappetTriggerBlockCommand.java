package mchorse.mappet.api.scripts.code.mappet.triggers.triggerBlocks;

import mchorse.mappet.api.scripts.code.mappet.triggers.MappetTriggerBlock;
import mchorse.mappet.api.scripts.user.mappet.triggers.triggerBlocks.IMappetTriggerBlockCommand;
import mchorse.mappet.api.triggers.blocks.AbstractTriggerBlock;
import mchorse.mappet.api.triggers.blocks.CommandTriggerBlock;

import java.util.List;

public class MappetTriggerBlockCommand extends MappetTriggerBlock<AbstractTriggerBlock> implements IMappetTriggerBlockCommand
{

    public MappetTriggerBlockCommand(CommandTriggerBlock block, List<AbstractTriggerBlock> blocks)
    {
        super(block, blocks);
    }

    @Override
    public CommandTriggerBlock getMinecraftScriptTriggerBlock()
    {
        return (CommandTriggerBlock) block;
    }

    @Override
    public void set(String command)
    {
        CommandTriggerBlock commandTriggerBlock = new CommandTriggerBlock(command);
        blocks.add(commandTriggerBlock);
    }
}
