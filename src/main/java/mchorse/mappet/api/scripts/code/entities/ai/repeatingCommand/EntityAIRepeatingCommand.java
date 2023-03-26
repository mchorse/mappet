package mchorse.mappet.api.scripts.code.entities.ai.repeatingCommand;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIBase;

public class EntityAIRepeatingCommand extends EntityAIBase
{
    private Entity entity;
    private String command;
    private int executionInterval;
    private int tickCounter;

    public EntityAIRepeatingCommand(Entity entity, String command, int executionInterval) {
        this.entity = entity;
        this.command = command;
        this.executionInterval = executionInterval;
        this.tickCounter = 0;
    }

    @Override
    public boolean shouldExecute() {
        return true;
    }

    @Override
    public void updateTask() {
        tickCounter++;
        if (tickCounter >= executionInterval) {
            this.entity.getServer().getCommandManager().executeCommand(this.entity, this.command);
            tickCounter = 0;
        }
    }

    public String getCommand() {
        return command;
    }
}