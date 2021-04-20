package mchorse.mappet.entities.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.util.EnumHand;

public class EntityAIAttackNpcMelee extends EntityAIAttackMelee
{
    public EntityAIAttackNpcMelee(EntityCreature creature, double speed, boolean useLongMemory)
    {
        super(creature, speed, useLongMemory);
    }

    @Override
    protected void checkAndPerformAttack(EntityLivingBase target, double delay)
    {
        double d0 = this.getAttackReachSqr(target);

        if (delay <= d0 && this.attackTick <= 0)
        {
            this.attackTick = 10;
            this.attacker.swingArm(EnumHand.MAIN_HAND);
            this.attacker.attackEntityAsMob(target);
        }
    }
}
