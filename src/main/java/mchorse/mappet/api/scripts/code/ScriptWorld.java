package mchorse.mappet.api.scripts.code;

import mchorse.mappet.api.scripts.code.blocks.ScriptBlockState;
import mchorse.mappet.api.scripts.user.IScriptEntity;
import mchorse.mappet.api.scripts.user.blocks.IScriptBlockState;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class ScriptWorld implements IScriptWorld
{
    private World world;

    public ScriptWorld(World world)
    {
        this.world = world;
    }

    @Override
    public World getMinecraftWorld()
    {
        return this.world;
    }

    @Override
    public void setBlock(IScriptBlockState state, int x, int y, int z)
    {
        this.world.setBlockState(new BlockPos(x, y, z), state.getMinecraftBlockState());
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

    @Override
    public long getTime()
    {
        return this.world.getWorldTime();
    }

    @Override
    public void setTime(long time)
    {
        this.world.setWorldTime(time);
    }

    @Override
    public long getTotalTime()
    {
        return this.world.getTotalWorldTime();
    }

    @Override
    public int getDimensionId()
    {
        Integer[] ids = DimensionManager.getIDs();

        for (int id : ids)
        {
            World world = this.world.getMinecraftServer().getWorld(id);

            if (world == this.world)
            {
                return id;
            }
        }

        return 0;
    }

    @Override
    public void spawnParticles(EnumParticleTypes type, boolean longDistance, double x, double y, double z, int n, double dx, double dy, double dz, double speed, int... args)
    {
        ((WorldServer) this.world).spawnParticle(type, longDistance, x, y, z, n, dx, dy, dz, speed, args);
    }

    @Override
    public void spawnParticles(IScriptEntity entity, EnumParticleTypes type, boolean longDistance, double x, double y, double z, int n, double dx, double dy, double dz, double speed, int... args)
    {
        if (entity == null || !entity.isPlayer())
        {
            return;
        }

        ((WorldServer) this.world).spawnParticle((EntityPlayerMP) entity.getMinecraftEntity(), type, longDistance, x, y, z, n, dx, dy, dz, speed, args);
    }
}