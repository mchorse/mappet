package mchorse.mappet.entities.ai;

import mchorse.mappet.entities.EntityNpc;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;

public class EntityAIReturnToPost extends EntityAIBase
{
    private final EntityNpc target;
    private BlockPos post;
    private final double speed;
    private int timer;
    private float min;
    private float prevWaterFactor;

    public EntityAIReturnToPost(EntityNpc target, BlockPos post, double followSpeedIn, float minDistIn)
    {
        this.target = target;
        this.post = post;
        this.speed = followSpeedIn;
        this.min = minDistIn;

        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
        if (this.target.getAttackTarget() != null)
        {
            return false;
        }

        return this.target.getDistanceSq(this.post) > this.min * this.min;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        if (this.target.getAttackTarget() != null)
        {
            return false;
        }

        return !this.target.getNavigator().noPath() && this.target.getDistanceSq(this.post) > this.min * this.min;
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
        this.target.getNavigator().clearPath();

        this.target.setPathPriority(PathNodeType.WATER, this.prevWaterFactor);
        this.target.rotationPitch = 0;
    }

    @Override
    public void updateTask()
    {
        int x = this.post.getX();
        int y = this.post.getY();
        int z = this.post.getZ();

        this.target.getLookHelper().setLookPosition(x, y + this.target.height, z, 10, this.target.getVerticalFaceSpeed());

        if (this.timer > 0)
        {
            this.timer--;

            return;
        }

        PathNavigate navigator = this.target.getNavigator();
        Path path = navigator.getPathToXYZ(x, y, z);

        navigator.setPath(path, this.speed);
        this.timer = 10;
    }
}
