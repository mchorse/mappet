package mchorse.mappet.api.scripts.code.entities.ai;

import mchorse.mclib.utils.MathUtils;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.ArrayUtils;

public class EntitiesAIPatrol extends EntityAIBase
{
    private final EntityLiving target;

    private final double speed;

    private int timer;

    private float prevWaterFactor;

    private int index;

    private int direction = 1;

    private BlockPos[] patrolPoints;

    private boolean[] shouldCirculate;

    private String[] executeCommandOnArrival;

    public EntitiesAIPatrol(EntityLiving target, double speed, BlockPos[] patrolPoints, boolean[] shouldCirculate, String[] executeCommandOnArrival)
    {
        this.target = target;
        this.speed = speed;
        this.patrolPoints = patrolPoints;
        this.shouldCirculate = shouldCirculate;
        this.executeCommandOnArrival = executeCommandOnArrival;

        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
        EntityLivingBase target = this.target.getAttackingEntity();

        return target == null && patrolPoints.length > 0;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        EntityLivingBase target = this.target.getAttackingEntity();

        return target == null && patrolPoints.length > 0;
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
        if (this.index < 0 || this.index >= patrolPoints.length)
        {
            return;
        }

        BlockPos pos = patrolPoints[this.index];

        if (this.target.getDistanceSq(pos) < 2)
        {
            int next = this.index + this.direction;

            if (shouldCirculate[index])
            {
                this.index = MathUtils.cycler(this.index + this.direction, 0, patrolPoints.length - 1);
            }
            else
            {
                if (next < 0 || next >= patrolPoints.length)
                {
                    this.direction *= -1;
                }

                this.index += this.direction;
            }

            // Execute command on arrival if specified
            if (executeCommandOnArrival[index] != null)
            {
                this.target.getServer().getCommandManager().executeCommand(this.target, executeCommandOnArrival[index]);
            }
            this.timer = 0;
        }

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        this.target.getLookHelper().setLookPosition(x, y + this.target.height, z, 10, this.target.getVerticalFaceSpeed());

        if (--this.timer <= 0)
        {
            this.timer = 10;

            Path path = this.target.getNavigator().getPathToXYZ(x, y, z);

            if (path != null)
            {
                this.target.getNavigator().setPath(path, this.speed);
            }
        }
    }

    public BlockPos[] getPatrolPoints()
    {
        return this.patrolPoints;
    }

    public boolean[] getShouldCirculate()
    {
        return this.shouldCirculate;
    }

    public String[] getExecuteCommandOnArrival()
    {
        return this.executeCommandOnArrival;
    }

    public void addPatrolPoint(BlockPos point, boolean shouldCirculate, String executeCommandOnArrival)
    {
        this.patrolPoints = ArrayUtils.add(this.patrolPoints, point);
        this.shouldCirculate = ArrayUtils.add(this.shouldCirculate, shouldCirculate);
        this.executeCommandOnArrival = ArrayUtils.add(this.executeCommandOnArrival, executeCommandOnArrival);
    }
}
