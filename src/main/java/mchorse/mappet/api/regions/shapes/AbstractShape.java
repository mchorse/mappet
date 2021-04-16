package mchorse.mappet.api.regions.shapes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;

import javax.vecmath.Vector3d;

public abstract class AbstractShape implements IShape
{
    public Vector3d pos;

    public void setPos(double x, double y, double z)
    {
        this.pos.set(x, y, z);
    }

    @Override
    public void setInitialPos(BlockPos pos)
    {
        if (this.pos == null)
        {
            this.pos = new Vector3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        }
    }

    @Override
    public boolean isPlayerInside(EntityPlayer player)
    {
        if (this.pos == null)
        {
            return false;
        }

        return this.isInside(player.posX, player.posY, player.posZ);
    }

    public abstract boolean isInside(double x, double y, double z);

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setDouble("PosX", this.pos.x);
        tag.setDouble("PosY", this.pos.y);
        tag.setDouble("PosZ", this.pos.z);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("PosX") && tag.hasKey("PosY") && tag.hasKey("PosZ"))
        {
            this.pos = new Vector3d(
                tag.getDouble("PosX"),
                tag.getDouble("PosY"),
                tag.getDouble("PosZ")
            );
        }
    }
}