package mchorse.mappet.api.scripts.code.blocks;

import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.mappet.api.scripts.user.blocks.IScriptBlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class ScriptBlockState implements IScriptBlockState
{
    public static ScriptBlockState AIR = new ScriptBlockState(Blocks.AIR.getDefaultState());
    public static BlockPos.MutableBlockPos BLOCK_POS = new BlockPos.MutableBlockPos();

    private IBlockState state;

    public static IScriptBlockState create(IBlockState state)
    {
        if (state == Blocks.AIR.getDefaultState() || state == null)
        {
            return AIR;
        }

        return new ScriptBlockState(state);
    }

    private ScriptBlockState(IBlockState state)
    {
        this.state = state;
    }

    @Override
    public IBlockState getMinecraftBlockState()
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
        ScriptBlockState otherState = (ScriptBlockState) state;
        return this.state.getBlock() == otherState.state.getBlock() && this.getMeta() == otherState.getMeta();
    }

    @Override
    public boolean isSameBlock(IScriptBlockState state)
    {
        return this.state.getBlock() == ((ScriptBlockState) state).state.getBlock();
    }

    @Override
    public boolean isOpaque()
    {
        return this.state.isOpaqueCube();
    }

    @Override
    public boolean hasCollision(IScriptWorld world, int x, int y, int z)
    {
        return this.state.getCollisionBoundingBox(world.getMinecraftWorld(), BLOCK_POS.setPos(x, y, z)) != null;
    }

    @Override
    public boolean isAir()
    {
        return this.state.getBlock() == Blocks.AIR;
    }
}