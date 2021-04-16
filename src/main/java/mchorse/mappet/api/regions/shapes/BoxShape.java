package mchorse.mappet.api.regions.shapes;

import net.minecraft.nbt.NBTTagCompound;

import javax.vecmath.Vector3d;

public class BoxShape extends AbstractShape
{
    public Vector3d size = new Vector3d(1, 1, 1);

    @Override
    public boolean isInside(double x, double y, double z)
    {
        double dx = x - this.pos.x;
        double dy = y - this.pos.y;
        double dz = z - this.pos.z;

        return Math.abs(dx) < this.size.x && Math.abs(dy) < this.size.y && Math.abs(dz) < this.size.z;
    }

    @Override
    public String getType()
    {
        return "box";
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        tag.setDouble("SizeX", this.size.x);
        tag.setDouble("SizeY", this.size.y);
        tag.setDouble("SizeZ", this.size.z);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("SizeX") && tag.hasKey("SizeY") && tag.hasKey("SizeZ"))
        {
            this.size.x = tag.getDouble("SizeX");
            this.size.y = tag.getDouble("SizeY");
            this.size.z = tag.getDouble("SizeZ");
        }
    }
}