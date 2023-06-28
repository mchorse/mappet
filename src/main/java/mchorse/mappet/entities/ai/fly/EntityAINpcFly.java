package mchorse.mappet.entities.ai.fly;

import mchorse.mappet.entities.EntityNpc;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;

import java.util.Random;

public class EntityAINpcFly extends EntityAIBase {
    private EntityCreature creature;
    private double speed;
    private Random rand;
    private double maxHeight;
    private double minHeight;
    private int stuckTimer;
    private int timeUntilNewDirection;
    private double targetX, targetY, targetZ;

    public EntityAINpcFly(EntityCreature creature) {
        this.creature = creature;
        this.speed = ((EntityNpc) creature).getState().speed.get() / 2D;
        this.minHeight = ((EntityNpc) creature).getState().flightMinHeight.get();
        this.maxHeight = ((EntityNpc) creature).getState().flightMaxHeight.get();
        this.rand = new Random();
        this.setMutexBits(1);
    }

    @Override
    public boolean shouldExecute() {
        return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        return !this.creature.onGround;
    }

    @Override
    public void updateTask() {
        if (--this.timeUntilNewDirection <= 0 || this.creature.getNavigator().noPath()) {
            this.timeUntilNewDirection += this.rand.nextInt(30) + 5;
            this.targetX = this.creature.posX + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F;

            // Find ground level
            RayTraceResult rayTrace = this.creature.world.rayTraceBlocks(new Vec3d(this.creature.posX, this.creature.posY, this.creature.posZ),
                    new Vec3d(this.creature.posX, 0, this.creature.posZ), false, true, false);
            double groundLevel = rayTrace != null && rayTrace.hitVec != null ? rayTrace.hitVec.y : this.creature.posY;

            // Set targetY between minHeight and maxHeight above the ground
            this.targetY = groundLevel + this.minHeight + (this.maxHeight - this.minHeight) * this.rand.nextFloat();

            this.targetZ = this.creature.posZ + (this.rand.nextFloat() * 2.0F - 1.0F) * 16.0F;
        }

        double dx = this.targetX - this.creature.posX;
        double dy = this.targetY - this.creature.posY;
        double dz = this.targetZ - this.creature.posZ;
        double dist = dx * dx + dy * dy + dz * dz;

        if (dist < 2.0F || dist > 256.0F || this.stuckTimer > 120) {
            // If too close, too far, or stuck for too long, find a new target
            this.stuckTimer = 0;
            this.timeUntilNewDirection = 0;
        } else {
            this.stuckTimer++;
        }

        this.creature.getMoveHelper().setMoveTo(this.targetX, this.targetY, this.targetZ, this.speed);
    }
}
