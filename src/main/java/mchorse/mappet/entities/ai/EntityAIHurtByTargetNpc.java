package mchorse.mappet.entities.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIHurtByTarget;

public class EntityAIHurtByTargetNpc extends EntityAIHurtByTarget
{
    public boolean reset;

    public EntityAIHurtByTargetNpc(EntityCreature creatureIn, boolean entityCallsForHelpIn, Class<?>... excludedReinforcementTypes)
    {
        super(creatureIn, entityCallsForHelpIn, excludedReinforcementTypes);
    }

    @Override
    public boolean shouldContinueExecuting()
    {
        if (this.reset)
        {
            this.reset = false;

            return false;
        }

        return super.shouldContinueExecuting();
    }
}