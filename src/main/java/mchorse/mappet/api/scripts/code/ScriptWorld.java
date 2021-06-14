package mchorse.mappet.api.scripts.code;

import mchorse.mappet.api.scripts.code.blocks.ScriptBlockState;
import mchorse.mappet.api.scripts.code.entities.ScriptEntity;
import mchorse.mappet.api.scripts.code.items.ScriptInventory;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.mappet.api.scripts.user.blocks.IScriptBlockState;
import mchorse.mappet.api.scripts.user.entities.IScriptEntity;
import mchorse.mappet.api.scripts.user.items.IScriptInventory;
import mchorse.mappet.api.scripts.user.items.IScriptItemStack;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumParticleTypes;
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
    public static final int MAX_VOLUME = 100;

    private World world;
    private BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

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
        if (!this.world.isBlockLoaded(this.pos.setPos(x, y, z)))
        {
            return;
        }

        this.world.setBlockState(this.pos, state.getMinecraftBlockState());
    }

    @Override
    public IScriptBlockState getBlock(int x, int y, int z)
    {
        if (this.world.isBlockLoaded(this.pos.setPos(x, y, z)))
        {
            return null;
        }

        return new ScriptBlockState(this.world.getBlockState(this.pos));
    }

    @Override
    public boolean hasInventory(int x, int y, int z)
    {
        this.pos.setPos(x, y, z);

        return this.world.isBlockLoaded(this.pos) && this.world.getTileEntity(this.pos) instanceof IInventory;
    }

    @Override
    public IScriptInventory getInventory(int x, int y, int z)
    {
        if (this.world.isBlockLoaded(this.pos.setPos(x, y, z)))
        {
            TileEntity tile = this.world.getTileEntity(this.pos);

            if (tile instanceof IInventory)
            {
                return new ScriptInventory((IInventory) tile);
            }
        }

        return null;
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
        if (!this.world.isBlockLoaded(this.pos.setPos(x, y, z)))
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

        return entity == null ? null : ScriptEntity.create(entity);
    }

    @Override
    public List<IScriptEntity> getEntities(double x1, double y1, double z1, double x2, double y2, double z2)
    {
        List<IScriptEntity> entities = new ArrayList<IScriptEntity>();

        double minX = Math.min(x1, x2);
        double minY = Math.min(y1, y2);
        double minZ = Math.min(z1, z2);
        double maxX = Math.max(x1, x2);
        double maxY = Math.max(y1, y2);
        double maxZ = Math.max(z1, z2);

        if (maxX - minX > MAX_VOLUME || maxY - minY > MAX_VOLUME || maxZ - minZ > MAX_VOLUME)
        {
            return entities;
        }

        if (!this.world.isBlockLoaded(this.pos.setPos(minX, minY, minZ)) || !this.world.isBlockLoaded(this.pos.setPos(maxX, maxY, maxZ)))
        {
            return entities;
        }

        for (Entity entity : this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ)))
        {
            entities.add(ScriptEntity.create(entity));
        }

        return entities;
    }

    @Override
    public List<IScriptEntity> getEntities(double x, double y, double z, double radius)
    {
        radius = Math.abs(radius);
        List<IScriptEntity> entities = new ArrayList<IScriptEntity>();

        if (radius > MAX_VOLUME / 2)
        {
            return entities;
        }

        double minX = x - radius;
        double minY = y - radius;
        double minZ = z - radius;
        double maxX = x + radius;
        double maxY = y + radius;
        double maxZ = z + radius;

        if (!this.world.isBlockLoaded(this.pos.setPos(minX, minY, minZ)) || !this.world.isBlockLoaded(this.pos.setPos(maxX, maxY, maxZ)))
        {
            return entities;
        }

        for (Entity entity : this.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ)))
        {
            AxisAlignedBB box = entity.getEntityBoundingBox();
            double eX = (box.minX + box.maxX) / 2D;
            double eY = (box.minY + box.maxY) / 2D;
            double eZ = (box.minZ + box.maxZ) / 2D;

            double dX = x - eX;
            double dY = y - eY;
            double dZ = z - eZ;

            if (dX * dX + dY * dY + dZ * dZ < radius * radius)
            {
                entities.add(ScriptEntity.create(entity));
            }
        }

        return entities;
    }

    @Override
    public void playSound(String event, double x, double y, double z, float volume, float pitch)
    {
        for (EntityPlayerMP player : this.world.getMinecraftServer().getPlayerList().getPlayers())
        {
            WorldUtils.playSound(player, event, x, y, z, volume, pitch);
        }
    }

    @Override
    public IScriptEntity dropItemStack(IScriptItemStack stack, double x, double y, double z, double mx, double my, double mz)
    {
        if (stack == null || stack.isEmpty())
        {
            return null;
        }

        EntityItem item = new EntityItem(this.world, x, y, z, stack.getMinecraftItemStack().copy());

        item.motionX = mx;
        item.motionY = my;
        item.motionZ = mz;

        this.world.spawnEntity(item);

        return ScriptEntity.create(item);
    }
}