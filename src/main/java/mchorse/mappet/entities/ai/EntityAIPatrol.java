package mchorse.mappet.entities.ai;

import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.entities.EntityNpc;
import mchorse.mclib.utils.MathUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.pathfinding.PathNodeType;
import net.minecraft.util.math.BlockPos;

public class EntityAIPatrol extends EntityAIBase
{
    private final EntityNpc target;
    private final double speed;
    private int timer;
    private float prevWaterFactor;

    private int index;
    private int direction = 1;

    public EntityAIPatrol(EntityNpc target, double speed)
    {
        this.target = target;
        this.speed = speed;

        this.setMutexBits(3);
    }

    @Override
    public boolean shouldExecute()
    {
        EntityLivingBase target = this.target.getAttackTarget();

        return target == null && !this.target.getState().patrol.isEmpty();
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        EntityLivingBase target = this.target.getAttackTarget();

        return target == null && !this.target.getState().patrol.isEmpty();
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
        NpcState state = this.target.getState();

        if (this.index < 0 || this.index >= state.patrol.size())
        {
            return;
        }

        BlockPos pos = state.patrol.get(this.index);

        if (this.target.getDistanceSq(pos) < 2)
        {
            int next = this.index + this.direction;

            if (state.patrolCirculate)
            {
                this.index = MathUtils.cycler(this.index + this.direction, 0, state.patrol.size() - 1);
            }
            else
            {
                if (next < 0 || next >= this.target.getState().patrol.size())
                {
                    this.direction *= -1;
                }

                this.index += this.direction;
            }

            this.timer = 0;
        }

        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

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
