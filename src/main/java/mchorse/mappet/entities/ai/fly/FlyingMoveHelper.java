package mchorse.mappet.entities.ai.fly;

import mchorse.mappet.entities.EntityNpc;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.MathHelper;

public class FlyingMoveHelper extends EntityMoveHelper {
    private final EntityCreature entity;
    private double posX;
    private double posY;
    private double posZ;
    private double speed;
    public boolean update;

    public FlyingMoveHelper(EntityNpc entity) {
        super(entity);
        this.entity = entity;
        this.posX = entity.posX;
        this.posY = entity.posY;
        this.posZ = entity.posZ;
    }

    public void onUpdateMoveHelper() {
        if (this.update) {
            this.update = false;

            double speed = this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue();
            double verticalSpeed = this.speed * (((EntityNpc) entity).getState().speed)/8.0D;
            this.entity.setAIMoveSpeed((float)speed);

            double d0 = this.posX - this.entity.posX;
            double d1 = this.posY - this.entity.posY;
            double d2 = this.posZ - this.entity.posZ;
            double d4 = d0 * d0 + d1 * d1 + d2 * d2;
            double d5 = Math.sqrt(d4);
            speed = Math.min(d5/5.0D, speed);

            if (this.entity.hurtTime == 0 && d4 > 0.5D) {
                this.entity.motionX += (speed * (d0 / d5) - this.entity.motionX) * speed;
                this.entity.motionZ += (speed * (d2 / d5) - this.entity.motionZ) * speed;

                this.entity.motionY = verticalSpeed * (d1 / d5);
                if (this.entity.motionY > 0) {
                    this.entity.motionY += 0.1D;
                }

                this.entity.velocityChanged = true;
            }

            this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, (float) ((Math.atan2(-d0, -d2) + Math.PI) * -(180F / Math.PI)), 20.0F);
        }
    }

    @Override
    protected float limitAngle(float angleIn, float lower, float upper) {
        float f = MathHelper.wrapDegrees(lower - angleIn);

        if (f > upper) {
            f = upper;
        }

        if (f < -upper) {
            f = -upper;
        }

        float f1 = angleIn + f;

        return f1;
    }

    /**
     * Sets the speed and location to move to
     */
    @Override
    public void setMoveTo(double x, double y, double z, double speedIn) {
        this.posX = x;
        this.posY = y;
        this.posZ = z;
        this.speed = speedIn;
        this.update = true;
    }

    public boolean isUpdating() {
        return this.update;
    }

    public double getSpeed() {
        return this.speed;
    }
}
