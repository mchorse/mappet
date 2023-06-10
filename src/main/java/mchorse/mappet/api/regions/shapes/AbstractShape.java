package mchorse.mappet.api.regions.shapes;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.INBTSerializable;

import javax.vecmath.Vector3d;

/**
 * Abstract shape class
 *
 * This base class provides base of operation for region's shapes
 */
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

    public boolean isEntityInside(Entity entity, BlockPos tile)
    {
        if (this.pos == null)
        {
            return false;
        }

        return this.isEntityInside(entity.posX, (entity.posY + entity.height / 2), entity.posZ, tile);
    }

    public boolean isEntityInside(double x, double y, double z, BlockPos tile)
    {
        if (this.pos == null)
        {
            return false;
        }

        return this.isInside(x - tile.getX() - 0.5, y - tile.getY() - 0.5, z - tile.getZ() - 0.5);
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