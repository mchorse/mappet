package mchorse.mappet.entities.ai;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWanderAvoidWater;
import net.minecraft.util.math.Vec3d;

public class EntityAIAlwaysWander extends EntityAIWanderAvoidWater {

    public EntityAIAlwaysWander(EntityCreature creature, double speed) {
        super(creature, speed);
    }

    @Override
    public boolean shouldExecute() {
        // Always return true so the AI task is always chosen for execution
        return true;
    }

    @Override
    public boolean shouldContinueExecuting() {
        // Always return true so the AI task continues executing once started
        return true;
    }

    @Override
    public void updateTask() {
        super.updateTask();
        // Check if the entity has reached its destination
        if (this.entity.getNavigator().noPath()) {
            // Generate a new random path
            Vec3d position = this.getPosition();
            if (position != null) {
                this.entity.getNavigator().tryMoveToXYZ(position.x, position.y, position.z, this.speed);
            }
        }
    }
}
