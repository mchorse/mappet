package mchorse.mappet.entities.ai;

import mchorse.mappet.Mappet;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.util.EnumHand;
import net.minecraftforge.common.config.Config;

public class EntityAIAttackNpcMelee extends EntityAIAttackMelee {

    private int rate = 10;

    public static final int maxRate = 200;
    public static final int minRate = 0;
    private static final int maxTickSpeed = 0;
    private static final int minTickSpeed = 10;


    public EntityAIAttackNpcMelee(EntityCreature creature, double speed, boolean useLongMemory) {
        super(creature, speed, useLongMemory);
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    @Override
    protected void checkAndPerformAttack(EntityLivingBase target, double delay) {
        double d0 = this.getAttackReachSqr(target);
        if (delay <= d0 && this.attackTick <= 0) {
            this.attackTick = this.getTickFromRate(this.rate);
            this.attacker.swingArm(EnumHand.MAIN_HAND);
            this.attacker.attackEntityAsMob(target);
        }
    }

    private int getTickFromRate(int rate) {
        int onePoint = (maxRate-minRate) / maxTickSpeed;
        int i = rate - minRate;
        return i / onePoint;
    }
}
