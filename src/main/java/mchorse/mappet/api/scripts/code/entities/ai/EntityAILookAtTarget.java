package mchorse.mappet.api.scripts.code.entities.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAILookAtTarget extends EntityAIBase
{
    private final EntityLiving entity;

    private final Entity target;

    private final float chance;

    private int lookTime;

    public EntityAILookAtTarget(EntityLiving entity, Entity target, float chance)
    {
        this.entity = entity;
        this.target = target;
        this.chance = chance;

        this.setMutexBits(2);
    }

    @Override
    public boolean shouldExecute()
    {
        if (this.entity.getRNG().nextFloat() >= this.chance)
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        if (!this.target.isEntityAlive())
        {
            return false;
        }

        return this.lookTime > 0;
    }

    @Override
    public void startExecuting()
    {
        this.lookTime = 40 + this.entity.getRNG().nextInt(40);
    }

    @Override
    public void resetTask()
    {
        this.lookTime = 0;
    }

    @Override
    public void updateTask()
    {
        this.entity.getLookHelper().setLookPosition(this.target.posX, this.target.posY + (double) this.target.getEyeHeight(), this.target.posZ, (float) this.entity.getHorizontalFaceSpeed(), (float) this.entity.getVerticalFaceSpeed());
        this.lookTime--;
    }

    public Entity getTarget()
    {
        return this.target;
    }
}