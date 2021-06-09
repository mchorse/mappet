package mchorse.mappet.api.scripts.code;

import mchorse.mappet.api.scripts.code.blocks.ScriptBlockState;
import mchorse.mappet.api.scripts.user.IScriptEntity;
import mchorse.mappet.api.scripts.user.blocks.IScriptBlockState;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.storage.AnvilChunkLoader;
import net.minecraftforge.common.DimensionManager;

import java.util.ArrayList;
import java.util.List;

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

    @Override
    public IScriptEntity spawnEntity(String id, double x, double y, double z)
    {
        return this.spawnEntity(id, x, y, z, null);
    }

    @Override
    public IScriptEntity spawnEntity(String id, double x, double y, double z, INBTCompound compound)
    {
        if (!this.world.isBlockLoaded(new BlockPos(x, y, z)))
        {
            return null;
        }

        NBTTagCompound tag = new NBTTagCompound();

        if (compound != null)
        {
            tag.merge(compound.getNBTTagComound());
        }

        tag.setString("id", id);

        Entity entity = AnvilChunkLoader.readWorldEntityPos(tag, this.world, x, y, z, true);

        return entity == null ? null : new ScriptEntity(entity);
    }

    @Override
    public List<IScriptEntity> getEntities(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        final int maxVolume = 100;

        List<IScriptEntity> entities = new ArrayList<IScriptEntity>();

        double minX = Math.min(x1, x2);
        double minY = Math.min(y1, y2);
        double minZ = Math.min(z1, z2);
        double maxX = Math.max(x1, x2);
        double maxY = Math.max(y1, y2);
        double maxZ = Math.max(z1, z2);

        if (maxX - minX > maxVolume || maxY - minY > maxVolume || maxZ - minZ > maxVolume)
        {
            return entities;
        }

        if (!this.world.isBlockLoaded(new BlockPos(minX, minY, minZ)) || !this.world.isBlockLoaded(new BlockPos(maxX, maxY, maxZ)))
        {
            return entities;
        }

        for (Entity entity : this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ)))
        {
            entities.add(new ScriptEntity(entity));
        }

        return entities;
    }
}