package mchorse.mappet.api.scripts.user.data;

import javax.vecmath.Vector3d;

/**
 * Script box represents a box in the space
 */
public class ScriptBox
{
    /**
     * minX coordinate
     */
    public double minX;

    /**
     * minY coordinate
     */
    public double minY;

    /**
     * minZ coordinate
     */
    public double minZ;

    /**
     * maxX coordinate
     */
    public double maxX;

    /**
     * maxY coordinate
     */
    public double maxY;

    /**
     * maxZ coordinate
     */
    public double maxZ;

    public ScriptBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
    {
        if (minX > maxX)
        {
            double temp = minX;
            minX = maxX;
            maxX = temp;
        }

        if (minY > maxY)
        {
            double temp = minY;
            minY = maxY;
            maxY = temp;
        }

        if (minZ > maxZ)
        {
            double temp = minZ;
            minZ = maxZ;
            maxZ = temp;
        }

        this.minX = minX;
        this.minY = minY;
        this.minZ = minZ;

        this.maxX = maxX;
        this.maxY = maxY;
        this.maxZ = maxZ;
    }

    @Override
    public String toString()
    {
        return "ScriptBox(" + this.minX + ", " + this.minY + ", " + this.minZ + ", " + this.maxX + ", " + this.maxY + ", " + this.maxZ + ")";
    }

    public boolean isColliding(ScriptBox box)
    {
        return this.minX < box.maxX && this.maxX > box.minX && this.minY < box.maxY && this.maxY > box.minY && this.minZ < box.maxZ && this.maxZ > box.minZ;
    }

    public void offset(double x, double y, double z)
    {
        this.minX += x;
        this.minY += y;
        this.minZ += z;

        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
    }

    public boolean contains(double x, double y, double z)
    {
        return x >= this.minX && x <= this.maxX && y >= this.minY && y <= this.maxY && z >= this.minZ && z <= this.maxZ;
    }

    public boolean contains(ScriptVector vector)
    {
        return this.contains(vector.x, vector.y, vector.z);
    }

    public boolean contains(Vector3d vector)
    {
        return this.contains(vector.x, vector.y, vector.z);
    }
}
