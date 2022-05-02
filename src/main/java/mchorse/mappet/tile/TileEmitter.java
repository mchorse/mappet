package mchorse.mappet.tile;

import mchorse.mappet.api.conditions.Checker;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.blocks.BlockEmitter;
import mchorse.mappet.network.common.blocks.PacketEditEmitter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TileEmitter extends TileEntity implements ITickable
{
    private Checker checker = new Checker();
    private float radius;
    private int update = 5;
    private boolean disable;

    private int tick = 0;

    public TileEmitter()
    {}

    public Checker getChecker()
    {
        return this.checker;
    }

    public float getRadius()
    {
        return this.radius;
    }

    public int getUpdate()
    {
        return this.update;
    }

    public boolean getDisable()
    {
        return this.disable;
    }

    public void setExpression(PacketEditEmitter message)
    {
        this.checker.deserializeNBT(message.checker);
        this.radius = message.radius;
        this.update = Math.max(message.update, 1);
        this.disable = message.disable;
        this.updateExpression();
        this.markDirty();
    }

    @Override
    public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate)
    {
        return oldState.getBlock() != newSate.getBlock();
    }

    @Override
    public void update()
    {
        if (this.world.isRemote)
        {
            return;
        }

        if (this.tick % this.update == 0 && !this.checker.isEmpty())
        {
            this.updateExpression();
        }

        this.tick += 1;
    }

    private void updateExpression()
    {
        List<EntityPlayer> playersInside = new ArrayList<EntityPlayer>();

        if (this.radius > 0)
        {
            BlockPos pos = this.getPos();
            boolean playerIn = false;

            for (EntityPlayer player : this.world.playerEntities)
            {
                if (player.getDistance(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5) <= this.radius)
                {
                    playerIn = true;

                    playersInside.add(player);

                    break;
                }
            }

            if (!playerIn)
            {
                if (this.disable)
                {
                    this.updateState(false);
                }

                return;
            }
        }

        boolean result = this.checker.check(new DataContext(this.world, this.getPos()));

        /* Don't judge me, I only have one brain cell */
        if (!result)
        {
            for (EntityPlayer player : playersInside)
            {
                result = this.checker.check(new DataContext(player));

                if (result)
                {
                    break;
                }
            }
        }
        this.updateState(result);
    }

    private void updateState(boolean result)
    {
        IBlockState state = this.world.getBlockState(this.pos);

        if (state.getValue(BlockEmitter.POWERED) != result)
        {
            this.world.setBlockState(this.pos, state.withProperty(BlockEmitter.POWERED, result));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setTag("Checker", this.checker.serializeNBT());

        if (this.radius > 0)
        {
            tag.setFloat("Radius", this.radius);
        }

        if (this.update > 0)
        {
            tag.setInteger("Update", this.update);
        }

        if (this.disable)
        {
            tag.setBoolean("Disable", this.disable);
        }

        return super.writeToNBT(tag);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag)
    {
        super.readFromNBT(tag);

        if (tag.hasKey("Checker"))
        {
            this.checker.deserializeNBT(tag.getTag("Checker"));
        }

        if (tag.hasKey("Radius"))
        {
            this.radius = tag.getFloat("Radius");
        }

        if (tag.hasKey("Update"))
        {
            this.update = tag.getInteger("Update");
        }

        if (tag.hasKey("Disable"))
        {
            this.disable = tag.getBoolean("Disable");
        }
    }
}