package mchorse.mappet.api.scripts.code.mappet.triggers;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.scripts.code.mappet.triggers.triggerBlocks.MappetTriggerBlockCommand;
import mchorse.mappet.api.scripts.code.mappet.triggers.triggerBlocks.MappetTriggerBlockScript;
import mchorse.mappet.api.scripts.code.nbt.ScriptNBTCompound;
import mchorse.mappet.api.scripts.user.mappet.triggers.IMappetTriggerBlock;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.api.triggers.blocks.AbstractTriggerBlock;
import mchorse.mappet.api.triggers.blocks.CommandTriggerBlock;
import mchorse.mappet.api.triggers.blocks.ScriptTriggerBlock;

import java.util.List;

public class MappetTriggerBlock <T extends AbstractTriggerBlock> implements IMappetTriggerBlock
{
    protected T block;
    protected List<T> blocks;

    public static IMappetTriggerBlock create(AbstractTriggerBlock block, List<AbstractTriggerBlock> blocks)
    {
        if (block instanceof ScriptTriggerBlock)
        {
            return new MappetTriggerBlockScript((ScriptTriggerBlock) block, blocks);
        }
        if (block instanceof CommandTriggerBlock)
        {
            return new MappetTriggerBlockCommand((CommandTriggerBlock) block, blocks);
        }

        return null;
    }

    protected MappetTriggerBlock(T block, List<T> blocks)
    {
        this.block = block;
        this.blocks = blocks;
    }

    @Override
    public String getType()
    {
        return CommonProxy.getTriggerBlocks().getType(block);
    }

    @Override
    public void setFrequency(int frequency)
    {
        block.frequency = frequency;
    }

    @Override
    public int getFrequency()
    {
        return block.frequency;
    }

    @Override
    public boolean remove()
    {
        return blocks.remove(block);
    }

    @Override
    public INBTCompound toNBT()
    {
        return new ScriptNBTCompound(block.toNBT());
    }
}