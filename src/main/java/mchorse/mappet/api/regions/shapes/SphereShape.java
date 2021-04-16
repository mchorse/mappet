package mchorse.mappet.api.regions.shapes;

import net.minecraft.nbt.NBTTagCompound;

public class SphereShape extends AbstractShape
{
    public double horizontal = 1;
    public double vertical = 1;

    public SphereShape()
    {}

    public SphereShape(double horizontal, double vertical)
    {
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    @Override
    public void copyFrom(AbstractShape shape)
    {
        super.copyFrom(shape);

        if (shape instanceof BoxShape)
        {
            this.horizontal = ((BoxShape) shape).size.x;
            this.vertical = ((BoxShape) shape).size.y;
        }
        else if (shape instanceof SphereShape)
        {
            this.horizontal = ((SphereShape) shape).horizontal;
            this.vertical = ((SphereShape) shape).vertical;
        }
    }

    @Override
    public boolean isInside(double x, double y, double z)
    {
        double dx = x - this.pos.x;
        double dy = y - this.pos.y;
        double dz = z - this.pos.z;

        double rx = dx / this.horizontal;
        double ry = dy / this.vertical;
        double rz = dz / this.horizontal;

        return rx * rx + ry * ry + rz * rz <= 1;
    }

    @Override
    public String getType()
    {
        return "sphere";
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        tag.setDouble("Horizontal", this.horizontal);
        tag.setDouble("Vertical", this.vertical);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Horizontal"))
        {
            this.horizontal = tag.getDouble("Horizontal");
        }

        if (tag.hasKey("Vertical"))
        {
            this.vertical = tag.getDouble("Vertical");
        }
    }
}
