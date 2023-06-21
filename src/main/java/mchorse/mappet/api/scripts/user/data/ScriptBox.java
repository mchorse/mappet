package mchorse.mappet.api.scripts.user.data;

import mchorse.mappet.api.scripts.code.ScriptWorld;
import mchorse.mappet.api.scripts.code.blocks.ScriptBlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.vecmath.Vector3d;
import java.util.ArrayList;
import java.util.List;

/**
 * Script box represents a box in the space
 *
 * <pre>{@code
 * function main(c)
 * {
 *     var subject = c.getSubject();
 *     var subjectPosition = subject.getPosition();
 *     var box = mappet.box(-10, 4, -10, 10, 6, 10);
 *     if (box.contains(subjectPosition)){
 *         c.send("the player in in the box")
 *     }
 * }
 * }</pre>
 */
public class ScriptBox
{
    public double minX;
    public double minY;
    public double minZ;
    public double maxX;
    public double maxY;
    public double maxZ;

    public ScriptBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ)
    {
        this.minX = Math.min(minX, maxX);
        this.minY = Math.min(minY, maxY);
        this.minZ = Math.min(minZ, maxZ);
        this.maxX = Math.max(minX, maxX);
        this.maxY = Math.max(minY, maxY);
        this.maxZ = Math.max(minZ, maxZ);
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

    /**
     * Offsets the box by given coordinates
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var box = mappet.box(-10, 4, -10, 10, 6, 10);
     *     box.offset(10, 0, 10);
     *     c.send(box.toString()); // ScriptBox(0.0, 4.0, 0.0, 20.0, 6.0, 20.0)
     * }
     * }</pre>
     */
    public void offset(double x, double y, double z)
    {
        this.minX += x;
        this.minY += y;
        this.minZ += z;

        this.maxX += x;
        this.maxY += y;
        this.maxZ += z;
    }

    /**
     * Checks if given coordinates are inside of this box
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var box = mappet.box(-10, 4, -10, 10, 6, 10);
     *     if (box.contains(0, 4, 0)){
     *         c.send("this point is inside the box")
     *     }
     *     if (box.contains(0, 7, 0)){
     *         c.send("this point is outside the box")
     *     }
     * }
     * }</pre>
     */
    public boolean contains(double x, double y, double z)
    {
        return x >= this.minX && x <= this.maxX && y >= this.minY && y <= this.maxY && z >= this.minZ && z <= this.maxZ;
    }

    /**
     * Checks if given coordinates are inside of this box
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var subject = c.getSubject();
     *     var subjectPosition = subject.getPosition();
     *     var box = mappet.box(-10, 4, -10, 10, 6, 10);
     *     if (box.contains(subjectPosition)){
     *         c.send("the player in in the box")
     *     }
     * }
     * }</pre>
     */
    public boolean contains(ScriptVector vector)
    {
        return this.contains(vector.x, vector.y, vector.z);
    }

    public boolean contains(Vector3d vector)
    {
        return this.contains(vector.x, vector.y, vector.z);
    }

    /**
     * Returns a list of positions for blocks in the box that match a given block state in a given world.
     *
     * <pre>{@code
     * function main(c)
     * {
     *     var world = c.getWorld();
     *     var state = mappet.createBlockState("minecraft:stone");
     *     var box = mappet.box(-10, 4, -10, 10, 6, 10);
     *     var blockPositions = box.getBlocksPositions(world, state);
     *
     *     var blockPositions = "[";
     *     blockPositions.forEach(function(position) {
     *         blockPositions += position.toArrayString() + ", ";
     *     });
     *     blockPositions += "]";
     *     print(blockPositions);
     * }
     * }</pre>
     *
     * @param scriptWorld The world in which to look for blocks.
     * @param state The block state to match.
     * @return A list of positions for blocks that match the given block state.
     */
    public List<ScriptVector> getBlocksPositions(ScriptWorld scriptWorld, ScriptBlockState state)
    {
        World world = scriptWorld.getMinecraftWorld();
        IBlockState blockState = state.getMinecraftBlockState();

        List<ScriptVector> blocks = new ArrayList<>();

        for (double x = this.minX; x <= this.maxX; x++) {
            for (double y = this.minY; y <= this.maxY; y++) {
                for (double z = this.minZ; z <= this.maxZ; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    if (world.getBlockState(pos).equals(blockState)) {
                        blocks.add(new ScriptVector(x, y, z));
                    }
                }
            }
        }

        return blocks;
    }
}
