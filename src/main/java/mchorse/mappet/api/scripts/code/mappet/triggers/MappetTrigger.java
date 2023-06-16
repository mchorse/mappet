package mchorse.mappet.api.scripts.code.mappet.triggers;

import mchorse.mappet.api.scripts.code.mappet.triggers.triggerBlocks.MappetTriggerBlockCommand;
import mchorse.mappet.api.scripts.code.mappet.triggers.triggerBlocks.MappetTriggerBlockScript;
import mchorse.mappet.api.scripts.user.mappet.triggers.IMappetTrigger;
import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.api.triggers.blocks.AbstractTriggerBlock;
import mchorse.mappet.api.triggers.blocks.CommandTriggerBlock;
import mchorse.mappet.api.triggers.blocks.ScriptTriggerBlock;

import java.util.ArrayList;
import java.util.List;

public class MappetTrigger implements IMappetTrigger
{
    private List<AbstractTriggerBlock> blocks;

    public MappetTrigger() {
    }

    public MappetTrigger(Trigger trigger)
    {
        this.blocks = trigger.blocks;
    }

    @Override
    public Trigger getMinecraftTrigger()
    {
        return new Trigger(blocks);
    }

    @Override
    public MappetTrigger set(MappetTrigger trigger)
    {
        this.blocks = trigger.blocks;
        return this;
    }

    @Override
    public List<MappetTriggerBlock> getAllBlocks()
    {
        List<MappetTriggerBlock> allTriggerBlocks = new ArrayList<>();
        for (AbstractTriggerBlock block : blocks) {
            allTriggerBlocks.add(new MappetTriggerBlock(block, blocks));
        }
        return allTriggerBlocks;
    }

    /* scripts */
    @Override
    public MappetTriggerBlockScript addScriptBlock()
    {
        ScriptTriggerBlock scriptTriggerBlock = new ScriptTriggerBlock();
        blocks.add(scriptTriggerBlock);
        return new MappetTriggerBlockScript(scriptTriggerBlock, blocks);
    }

    /* commands */
    @Override
    public MappetTriggerBlockCommand addCommandBlock()
    {
        CommandTriggerBlock commandTriggerBlock = new CommandTriggerBlock();
        blocks.add(commandTriggerBlock);
        return new MappetTriggerBlockCommand(commandTriggerBlock, blocks);
    }
}
