package mchorse.mappet.entities.ai;

import mchorse.mappet.entities.EntityNpc;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class EntityAIFollowTarget extends EntityAIBase
{
    private static final double TELEPORT_DISTANCE = 144;

    private final EntityNpc target;
    private EntityLivingBase follow;
    private final double speed;
    private int timer;
    private float max;
    private float min;
    private float prevWaterFactor;

    public EntityAIFollowTarget(EntityNpc target, double speed, float min, float max)
    {
        this.target = target;
        this.speed = speed;
        this.min = min;
        this.max = max;

        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
        EntityLivingBase target = this.target.getFollowTarget();

        if (target == null)
        {
            return false;
        }
        else if (target instanceof EntityPlayer && ((EntityPlayer) target).isSpectator())
        {
            return false;
        }
        else if (this.target.getDistanceSq(target) < this.min * this.min)
        {
            return false;
        }

        this.follow = target;

        return true;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        return !this.target.getNavigator().noPath() && this.target.getDistanceSq(this.follow) > this.min * this.min;
    }

    @Override
    public void startExecuting()
    {
        this.timer = 0;
        this.prevWaterFactor = this.target.getPathPriority(PathNodeType.WATER);

        this.target.setPathPriority(PathNodeType.WATER, 0);
    }

    @Override
    public void resetTask()
    {
        this.follow = null;
        this.target.getNavigator().clearPath();

        this.target.setPathPriority(PathNodeType.WATER, this.prevWaterFactor);
    }

    @Override
    public void updateTask()
    {
        this.target.getLookHelper().setLookPositionWithEntity(this.follow, 10, this.target.getVerticalFaceSpeed());

        if (this.timer > 0)
        {
            this.timer--;

            return;
        }

        this.timer = 10;

        if ((!this.target.getNavigator().tryMoveToEntityLiving(this.follow, this.speed) &&
            !this.target.getLeashed() &&
            !this.target.isRiding()) ||
            this.target.getDistanceSq(this.follow) >= TELEPORT_DISTANCE
        ) {
            int x = MathHelper.floor(this.follow.posX) - 2;
            int z = MathHelper.floor(this.follow.posZ) - 2;
            int y = MathHelper.floor(this.follow.getEntityBoundingBox().minY);

            for (int bx = 0; bx <= 4; ++bx)
            {
                for (int bz = 0; bz <= 4; ++bz)
                {
                    if ((bx < 1 || bz < 1 || bx > 3 || bz > 3) && this.canTeleport(x, z, y, bx, bz))
                    {
                        this.target.setLocationAndAngles((double)((float)(x + bx) + 0.5F), (double)y, (double)((float)(z + bz) + 0.5F), this.target.rotationYaw, this.target.rotationPitch);
                        this.target.getNavigator().clearPath();

                        return;
                    }
                }
            }
        }
    }

    private boolean canTeleport(int x, int z, int y, int offsetX, int offsetY)
    {
        World world = this.target.world;
        BlockPos pos = new BlockPos(x + offsetX, y - 1, z + offsetY);
        IBlockState state = world.getBlockState(pos);

        return state.getBlockFaceShape(world, pos, EnumFacing.DOWN) == BlockFaceShape.SOLID && state.canEntitySpawn(this.target) && world.isAirBlock(pos.up()) && world.isAirBlock(pos.up(2));
    }
}
