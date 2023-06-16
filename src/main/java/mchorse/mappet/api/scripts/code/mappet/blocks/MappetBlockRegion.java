package mchorse.mappet.api.scripts.code.mappet.blocks;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.regions.shapes.BoxShape;
import mchorse.mappet.api.regions.shapes.CylinderShape;
import mchorse.mappet.api.regions.shapes.SphereShape;
import mchorse.mappet.api.scripts.code.mappet.conditions.MappetCondition;
import mchorse.mappet.api.scripts.code.mappet.triggers.MappetTrigger;
import mchorse.mappet.api.scripts.user.IScriptWorld;
import mchorse.mappet.api.scripts.user.mappet.blocks.IMappetBlockRegion;
import mchorse.mappet.tile.TileRegion;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.vecmath.Vector3d;

/**
 * Represents a region block that can be manipulated programmatically.
 */
public class MappetBlockRegion implements IMappetBlockRegion
{

    private TileRegion regionBlock;

    public static MappetBlockRegion create()
    {
        MappetBlockRegion regionBlock = new MappetBlockRegion();
        regionBlock.regionBlock.region.shapes.clear();
        return regionBlock;
    }

    public MappetBlockRegion()
    {
        this.regionBlock = new TileRegion();
    }

    public MappetBlockRegion(World world, BlockPos pos)
    {
        this.regionBlock = (TileRegion) world.getTileEntity(pos);
    }

    @Override
    public MappetBlockRegion setPassable(boolean isPassable)
    {
        this.regionBlock.region.passable = isPassable;
        return this;
    }

    @Override
    public MappetBlockRegion place(IScriptWorld world, int x, int y, int z)
    {
        BlockPos pos = new BlockPos(x, y, z);
        World mcWorld = world.getMinecraftWorld();

        if (mcWorld.getBlockState(pos).getBlock() != Blocks.AIR)
        {
            mcWorld.setBlockState(pos, Blocks.AIR.getDefaultState(), 2 | 4);
        }

        mcWorld.setBlockState(pos, Mappet.regionBlock.getDefaultState(), 2 | 4);

        if (mcWorld.getBlockState(pos).getBlock() == Mappet.regionBlock)
        {
            TileRegion tileRegion = (TileRegion) mcWorld.getTileEntity(pos);
            mcWorld.setTileEntity(pos, tileRegion);
            tileRegion.markDirty();
            this.regionBlock = tileRegion;
        }

        return this;
    }

    @Override
    public MappetBlockRegion setCheckEntities(boolean checkEntities)
    {
        this.regionBlock.region.checkEntities = checkEntities;
        return this;
    }

    @Override
    public MappetBlockRegion setDelay(int delay)
    {
        this.regionBlock.region.delay = delay;
        return this;
    }

    @Override
    public MappetBlockRegion setUpdateFrequency(int frequency)
    {
        this.regionBlock.region.update = frequency;
        return this;
    }

    @Override
    public MappetBlockRegion addBoxShape(
            double halfSizeX, double halfSizeY, double halfSizeZ,
            double offsetX, double offsetY, double offsetZ
    )
    {
        BoxShape boxShape = new BoxShape();
        boxShape.pos = new Vector3d(offsetX, offsetY, offsetZ);
        boxShape.size = new Vector3d(halfSizeX, halfSizeY, halfSizeZ);
        this.regionBlock.region.shapes.add(boxShape);
        return this;
    }

    @Override
    public MappetBlockRegion addBoxShape(double halfSizeX, double halfSizeY, double halfSizeZ)
    {
        return this.addBoxShape(halfSizeX, halfSizeY, halfSizeZ, 0, 0, 0);
    }

    @Override
    public MappetBlockRegion addSphereShape(
            double horizontalRadius, double verticalRadius,
            double offsetX, double offsetY, double offsetZ
    )
    {
        SphereShape sphereShape = new SphereShape();
        sphereShape.pos = new Vector3d(offsetX, offsetY, offsetZ);
        sphereShape.horizontal = horizontalRadius;
        sphereShape.vertical = verticalRadius;
        this.regionBlock.region.shapes.add(sphereShape);
        return this;
    }

    @Override
    public MappetBlockRegion addSphereShape(double horizontalRadius, double verticalRadius)
    {
        return this.addSphereShape(horizontalRadius, verticalRadius, 0, 0, 0);
    }

    @Override
    public MappetBlockRegion addCylinderShape(
            double radius, double height,
            double offsetX, double offsetY, double offsetZ
    )
    {
        CylinderShape cylinderShape = new CylinderShape();
        cylinderShape.pos = new Vector3d(offsetX, offsetY, offsetZ);
        cylinderShape.horizontal = radius;
        cylinderShape.vertical = height;
        this.regionBlock.region.shapes.add(cylinderShape);
        return this;
    }

    @Override
    public MappetBlockRegion addCylinderShape(double radius, double height)
    {
        return this.addCylinderShape(radius, height, 0, 0, 0);
    }

    @Override
    public MappetTrigger getOnEnterTrigger()
    {
        return new MappetTrigger(this.regionBlock.region.onEnter);
    }

    @Override
    public MappetTrigger getOnExitTrigger()
    {
        return new MappetTrigger(this.regionBlock.region.onExit);
    }

    @Override
    public MappetTrigger getOnTickTrigger()
    {
        return new MappetTrigger(this.regionBlock.region.onTick);
    }

    @Override
    public MappetCondition getCondition()
    {
        return new MappetCondition(this.regionBlock.region.enabled);
    }
}
