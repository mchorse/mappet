package mchorse.mappet.api.regions.shapes;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import javax.vecmath.Vector3d;

public abstract class AbstractShape implements INBTSerializable<NBTTagCompound>
{
    public Vector3d pos = new Vector3d();

    public static AbstractShape fromString(String string)
    {
        if (string.equals("box"))
        {
            return new BoxShape();
        }
        else if (string.equals("sphere"))
        {
            return new SphereShape();
        }
        else if (string.equals("cylinder"))
        {
            return new CylinderShape();
        }

        return null;
    }

    public void copyFrom(AbstractShape shape)
    {
        this.pos.set(shape.pos);
    }

    public void setPos(double x, double y, double z)
    {
        this.pos.set(x, y, z);
    }

    public boolean isPlayerInside(EntityPlayer player, BlockPos tile)
    {
        if (this.pos == null)
        {
            return false;
        }

        return this.isInside(player.posX - tile.getX() - 0.5, (player.posY + player.height / 2) - tile.getY() - 0.5, player.posZ - tile.getZ() - 0.5);
    }

    public abstract String getType();

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