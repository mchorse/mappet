package mchorse.mappet.api.scripts.user.data;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

/**
 * Script vector (position) represents a position in the space
 */
public class ScriptVector
{
    /**
     * X coordinate
     */
    public double x;

    /**
     * Y coordinate
     */
    public double y;

    /**
     * Z coordinate
     */
    public double z;

    public ScriptVector(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public ScriptVector(Vec3d vector)
    {
        this.x = vector.x;
        this.y = vector.y;
        this.z = vector.z;
    }

    public ScriptVector(BlockPos pos)
    {
        this.x = pos.getX();
        this.y = pos.getY();
        this.z = pos.getZ();
    }

    @Override
    public String toString()
    {
        return "ScriptVector(" + this.x + ", " + this.y + ", " + this.z + ")";
    }

    public ScriptVector add(ScriptVector other) {
        return new ScriptVector(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    public ScriptVector subtract(ScriptVector other) {
        return new ScriptVector(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    public ScriptVector multiply(double scalar) {
        return new ScriptVector(this.x * scalar, this.y * scalar, this.z * scalar);
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }

    public ScriptVector normalize() {
        double length = this.length();
        return new ScriptVector(this.x / length, this.y / length, this.z / length);
    }
}