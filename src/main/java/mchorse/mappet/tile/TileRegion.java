package mchorse.mappet.tile;

import mchorse.mappet.api.regions.Region;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class TileRegion extends TileEntity implements ITickable
{
    public Region region = new Region();

    private Set<UUID> players = new HashSet<UUID>(10);
    private int tick;

    public void set(NBTTagCompound tag)
    {
        this.region.deserializeNBT(tag);
    }

    @Override
    public void update()
    {
        if (this.world.isRemote)
        {
            return;
        }

        if (this.tick % 3 == 0)
        {
            this.checkRegion();
        }

        this.tick += 1;
    }

    private void checkRegion()
    {
        if (!this.region.isEnabled())
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
                    this.region.onEnter.trigger(player);
                    this.players.add(id);
                }
            }
            else if (wasInside)
            {
                this.region.onExit.trigger(player);
                this.players.remove(id);
            }
        }
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