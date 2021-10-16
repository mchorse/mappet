package mchorse.mappet.entities.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.util.EnumHand;

public class EntityAIAttackNpcMelee extends EntityAIAttackMelee {

    private int delay;

    public EntityAIAttackNpcMelee(EntityCreature creature, double speed, boolean useLongMemory, int delay) {
        super(creature, speed, useLongMemory);
        this.delay = delay;
    }

    @Override
    protected void checkAndPerformAttack(EntityLivingBase target, double delay) {
        double d0 = this.getAttackReachSqr(target);
        if (delay <= d0 && this.attackTick <= 0) {
            this.attackTick = this.delay;
            this.attacker.swingArm(EnumHand.MAIN_HAND);
            this.attacker.attackEntityAsMob(target);
        }
    }
}
