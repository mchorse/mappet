package mchorse.mappet.api.scripts.code;

import mchorse.mappet.api.scripts.user.IScriptBlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

public class ScriptBlockState implements IScriptBlockState
{
    private IBlockState state;

    public ScriptBlockState(IBlockState state)
    {
        this.state = state;
    }

    public IBlockState getState()
    {
        return this.state;
    }

    @Override
    public int getMeta()
    {
        return this.state.getBlock().getMetaFromState(this.state);
    }

    @Override
    public String getBlockId()
    {
        ResourceLocation rl = this.state.getBlock().getRegistryName();

        return rl == null ? "" : rl.toString();
    }

    @Override
    public boolean isSame(IScriptBlockState state)
    {
        return this.state == ((ScriptBlockState) state).state;
    }

    @Override
    public boolean isSameBlock(IScriptBlockState state)
    {
        return this.state.getBlock() == ((ScriptBlockState) state).state.getBlock();
    }
}