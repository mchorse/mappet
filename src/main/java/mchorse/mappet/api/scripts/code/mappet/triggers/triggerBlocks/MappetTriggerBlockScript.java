package mchorse.mappet.api.scripts.code.mappet.triggers.triggerBlocks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import mchorse.mappet.api.scripts.code.mappet.triggers.MappetTriggerBlock;
import mchorse.mappet.api.scripts.user.mappet.triggers.triggerBlocks.IMappetTriggerBlockScript;
import mchorse.mappet.api.triggers.blocks.AbstractTriggerBlock;
import mchorse.mappet.api.triggers.blocks.ScriptTriggerBlock;
import mchorse.mappet.utils.ScriptUtils;

import java.util.List;

public class MappetTriggerBlockScript extends MappetTriggerBlock<AbstractTriggerBlock> implements IMappetTriggerBlockScript
{

    public MappetTriggerBlockScript(ScriptTriggerBlock block, List<AbstractTriggerBlock> blocks)
    {
        super(block, blocks);
    }

    @Override
    public ScriptTriggerBlock getMinecraftScriptTriggerBlock()
    {
        return (ScriptTriggerBlock) block;
    }

    @Override
    public MappetTriggerBlockScript setScriptName(String scriptName)
    {
        getMinecraftScriptTriggerBlock().string = scriptName;
        return this;
    }

    @Override
    public String getScriptName()
    {
        return getMinecraftScriptTriggerBlock().string;
    }

    @Override
    public MappetTriggerBlockScript setFunctionName(String functionName)
    {
        getMinecraftScriptTriggerBlock().function = functionName;
        return this;
    }

    @Override
    public String getFunctionName()
    {
        return getMinecraftScriptTriggerBlock().function;
    }

    @Override
    public MappetTriggerBlockScript setCustomData(String customData)
    {
        getMinecraftScriptTriggerBlock().customData = customData;
        return this;
    }

    @Override
    public String getCustomData()
    {
        return getMinecraftScriptTriggerBlock().customData;
    }

    @Override
    public MappetTriggerBlockScript setInlineCode(ScriptObjectMirror script)
    {
        getMinecraftScriptTriggerBlock().inline = true;
        getMinecraftScriptTriggerBlock().code = ScriptUtils.getScriptContent(script);
        return this;
    }

    @Override
    public String getInlineCode()
    {
        return getMinecraftScriptTriggerBlock().code;
    }
}
