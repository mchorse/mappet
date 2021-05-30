package mchorse.mappet.api.scripts.code;

import mchorse.mappet.api.scripts.user.IScriptBlockState;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ScriptWorld implements IScriptWorld
{
    private World world;

    public ScriptWorld(World world)
    {
        this.world = world;
    }

    public World getWorld()
    {
        return this.world;
    }

    @Override
    public void setBlock(IScriptBlockState state, int x, int y, int z)
    {
        this.world.setBlockState(new BlockPos(x, y, z), ((ScriptBlockState) state).getState());
    }

    @Override
    public IScriptBlockState getBlock(int x, int y, int z)
    {
        IBlockState state = this.world.getBlockState(new BlockPos(x, y, z));

        return new ScriptBlockState(state);
    }

    @Override
    public boolean isRaining()
    {
        return this.world.getWorldInfo().isRaining();
    }

    @Override
    public void setRaining(boolean raining)
    {
        this.world.getWorldInfo().setRaining(raining);
    }
}