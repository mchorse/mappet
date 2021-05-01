package mchorse.mappet.tile;

import mchorse.mappet.api.regions.Region;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.AxisAlignedBB;
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

        if (this.tick % 3 == 0)
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
                    this.region.onEnter.trigger(player);
                }

                it.remove();
            }

            trigger.getValue().setValue(delay - 1);
        }
    }

    private void checkRegion()
    {
        if (!this.region.isEnabled(this.world))
        {
            return;
        }

        for (EntityPlayer player : this.world.playerEntities)
        {
            UUID id = player.getGameProfile().getId();
            boolean wasInside = this.players.contains(id);

            if (this.region.shape.isPlayerInside(player, this.getPos()))
            {
                if (!wasInside)
                {
                    if (this.region.delay > 0)
                    {
                        this.delays.put(id, new MutableInt(this.region.delay));
                    }
                    else
                    {
                        this.region.onEnter.trigger(player);
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
                    this.region.onExit.trigger(player);
                }

                this.players.remove(id);
            }
        }
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