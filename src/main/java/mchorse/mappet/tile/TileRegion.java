package mchorse.mappet.tile;

import mchorse.mappet.api.regions.Region;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.utils.PositionCache;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.mutable.MutableInt;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class TileRegion extends TileEntity implements ITickable
{
    public Region region = new Region();

    private Set<UUID> players = new HashSet<UUID>(10);
    private Map<UUID, MutableInt> delays = new HashMap<UUID, MutableInt>();
    private int tick;

    public void set(NBTTagCompound tag)
    {
        this.region = new Region();
        this.region.deserializeNBT(tag);

        this.markDirty();
    }

    @Override
    public void update()
    {
        if (this.world.isRemote)
        {
            return;
        }

        if (!this.delays.isEmpty())
        {
            this.checkDelays();
        }

        int frequency = Math.max(this.region.update, 1);

        if (this.tick % frequency == 0)
        {
            this.checkRegion();
        }

        this.tick += 1;
    }

    private void checkDelays()
    {
        Iterator<Map.Entry<UUID, MutableInt>> it = this.delays.entrySet().iterator();

        while (it.hasNext())
        {
            Map.Entry<UUID, MutableInt> trigger = it.next();
            int delay = trigger.getValue().intValue();

            if (delay <= 0)
            {
                UUID id = trigger.getKey();
                EntityPlayer player = this.world.getPlayerEntityByUUID(id);

                if (player != null)
                {
                    this.region.triggerEnter(player);
                }

                it.remove();
            }

            trigger.getValue().setValue(delay - 1);
        }
    }

    private void checkRegion()
    {
        for (EntityPlayer player : this.world.playerEntities)
        {
            boolean enabled = this.region.isEnabled(player);

            if (!this.region.passable)
            {
                enabled = !enabled;
            }

            if (player.isSpectator() || !enabled)
            {
                continue;
            }

            UUID id = player.getGameProfile().getId();
            boolean wasInside = this.players.contains(id);

            if (this.region.isPlayerInside(player, this.getPos()))
            {
                if (!this.region.passable)
                {
                    this.handlePassing(player);
                }
                else if (!wasInside)
                {
                    if (this.region.delay > 0)
                    {
                        this.delays.put(id, new MutableInt(this.region.delay));
                    }
                    else
                    {
                        this.region.triggerEnter(player);
                    }

                    this.players.add(id);
                }
            }
            else if (wasInside)
            {
                if (this.delays.containsKey(id))
                {
                    this.delays.remove(id);
                }
                else
                {
                    this.region.triggerExit(player);
                }

                this.players.remove(id);
            }
        }
    }

    private void handlePassing(EntityPlayer player)
    {
        ICharacter character = Character.get(player);
        Vec3d last = player.getPositionVector();

        if (character == null)
        {
            return;
        }

        PositionCache cache = character.getPositionCache();
        Vec3d vec = cache.lastPosition;

        if (vec != null && !this.region.isPlayerInside(vec.x, vec.y + player.height / 2, vec.z, this.getPos()))
        {
            this.teleportPlayer(player, vec, last);

            cache.resetLastPositionTimer();

            return;
        }

        vec = cache.lastLastPosition;

        if (vec != null && !this.region.isPlayerInside(vec.x, vec.y + player.height / 2, vec.z, this.getPos()))
        {
            this.teleportPlayer(player, vec, last);

            cache.resetLastPositionTimer();

            return;
        }

        if (vec == null)
        {
            return;
        }

        vec = vec.subtract(player.posX, player.posY, player.posZ);

        if (vec.lengthSquared() > 0)
        {
            vec = vec.normalize();
            vec = vec.scale(-0.5D);

            double x = player.posX;
            double y = player.posY;
            double z = player.posZ;

            while (this.region.isPlayerInside(player, this.getPos()))
            {
                player.posX += vec.x;
                player.posY += vec.y;
                player.posZ += vec.z;
            }

            vec = new Vec3d(x, y, z);

            player.posX = x;
            player.posY = y;
            player.posZ = z;

            this.teleportPlayer(player, vec, last);
        }

        cache.resetLastPositionTimer();
    }

    private void teleportPlayer(EntityPlayer player, Vec3d vec, Vec3d last)
    {
        player.setPositionAndUpdate(vec.x, vec.y, vec.z);

        Vec3d motion = last.subtract(player.getPositionVector()).scale(-0.5);

        if (motion.distanceTo(Vec3d.ZERO) < 0.5 * 0.5)
        {
            motion = motion.normalize();
        }

        double y = Math.abs(motion.y) < 0.01 ? 0.2 : motion.y;

        ((EntityPlayerMP) player).connection.sendPacket(new SPacketEntityVelocity(player.getEntityId(), motion.x, y, motion.z));
    }

    /* Rendering related stuff */

    @Override
    @SideOnly(Side.CLIENT)
    public AxisAlignedBB getRenderBoundingBox()
    {
        return TileEntity.INFINITE_EXTENT_AABB;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getMaxRenderDistanceSquared()
    {
        float range = 128;

        return range * range;
    }

    /* NBT stuff */

    @Override
    public NBTTagCompound getUpdateTag()
    {
        return this.writeToNBT(new NBTTagCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket()
    {
        return new SPacketUpdateTileEntity(this.pos, this.getBlockMetadata(), this.getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity packet)
    {
        this.readFromNBT(packet.getNbtCompound());
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setTag("Region", this.region.serializeNBT());

        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        if (tag.hasKey("Region"))
        {
            this.region.deserializeNBT(tag.getCompoundTag("Region"));
        }
    }
}