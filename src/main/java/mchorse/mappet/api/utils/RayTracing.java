package mchorse.mappet.api.utils;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.List;

public class RayTracing
{
    /**
     * Kind of like rayTrace method, but as well it takes into account entity
     * ray tracing
     */
    public static RayTraceResult rayTraceWithEntity(World world, double x1, double y1, double z1, double x2, double y2, double z2)
    {
        double blockDistance = new Vec3d(x2, y2, z2).subtract(x1, y1, z1).lengthVector();

        RayTraceResult result = rayTrace(world, x1, y1, z1, x2, y2, z2);
        Vec3d origin = new Vec3d(x1, y1, z1);

        if (result != null)
        {
            blockDistance = result.hitVec.distanceTo(origin);
        }

        Vec3d destination = new Vec3d(x2, y2, z2);
        Vec3d hit = null;
        Entity target = null;

        List<Entity> list = world.getEntitiesInAABBexcluding(null, new AxisAlignedBB(x1, y1, z1, x2, y2, z2), (entity) -> entity != null && entity.canBeCollidedWith());
        double entityDistance = blockDistance;

        for (int i = 0; i < list.size(); ++i)
        {
            Entity entity = list.get(i);

            AxisAlignedBB aabb = entity.getEntityBoundingBox().grow(entity.getCollisionBorderSize());
            RayTraceResult intercept = aabb.calculateIntercept(origin, destination);

            if (aabb.contains(origin))
            {
                if (entityDistance >= 0.0D)
                {
                    hit = intercept == null ? origin : intercept.hitVec;
                    target = entity;
                    entityDistance = 0.0D;
                }
            }
            else if (intercept != null)
            {
                double eyesDistance = origin.distanceTo(intercept.hitVec);

                if (eyesDistance < entityDistance || entityDistance == 0.0D)
                {
                    hit = intercept.hitVec;
                    target = entity;
                    entityDistance = eyesDistance;
                }
            }
        }

        if (target != null)
        {
            result = new RayTraceResult(target, hit);
        }

        return result;
    }

    /**
     * This method is extracted from {@link Entity} class, because it was marked
     * as client side only code.
     */
    public static RayTraceResult rayTrace(World world, double x1, double y1, double z1, double x2, double y2, double z2)
    {
        Vec3d eyePos = new Vec3d(x1, y1, z1);
        Vec3d eyeReach = new Vec3d(x2, y2, z2);

        return world.rayTraceBlocks(eyePos, eyeReach, false, false, true);
    }
}