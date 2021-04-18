package mchorse.mappet.utils;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class WorldUtils
{
    public static TileEntity getTileEntity(World world, BlockPos pos)
    {
        if (world.isBlockLoaded(pos))
        {
            return world.getTileEntity(pos);
        }

        return null;
    }
}
