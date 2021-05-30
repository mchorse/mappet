package mchorse.mappet.api.scripts.user.data;

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
}