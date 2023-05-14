package mchorse.mappet.api.scripts.code.entities;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.api.scripts.user.entities.IScriptNpc;
import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.api.triggers.blocks.AbstractTriggerBlock;
import mchorse.mappet.api.triggers.blocks.ScriptTriggerBlock;
import mchorse.mappet.entities.EntityNpc;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.util.math.BlockPos;

public class ScriptNpc extends ScriptEntity<EntityNpc> implements IScriptNpc
{
    public ScriptNpc(EntityNpc entity)
    {
        super(entity);
    }

    @Override
    public EntityNpc getMappetNpc()
    {
        return this.entity;
    }

    @Override
    public String getNpcId()
    {
        return this.entity.getId();
    }

    @Override
    public boolean setMorph(AbstractMorph morph)
    {
        this.entity.getState().morph = MorphUtils.copy(morph);
        this.entity.setMorph(this.entity.getState().morph);
        this.entity.sendNpcStateChangePacket();

        return true;
    }

    @Override
    public String getNpcState()
    {
        return this.entity.getState().stateName;
    }

    @Override
    public void setNpcState(String stateId)
    {
        String npcId = this.entity.getId();
        Npc npc = Mappet.npcs.load(npcId);
        NpcState state = npc == null ? null : npc.states.get(stateId);

        if (npc != null && state == null && npc.states.containsKey("default"))
        {
            state = npc.states.get("default");
        }

        if (state != null)
        {
            this.entity.setNpc(npc, state);

            if (!npc.serializeNBT().getString("StateName").equals("default"))
            {
                this.entity.setStringInData("StateName", stateId);
            }
        }

        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public void canPickUpLoot(boolean canPickUpLoot)
    {
        this.entity.setCanPickUpLoot(canPickUpLoot);
    }

    @Override
    public void follow(String target)
    {
        NpcState state = this.entity.getState();

        state.follow = target;

        this.entity.setState(state, false);
    }

    @Override
    public void setOnTickTrigger(String scriptName, String functionName, int frequency, int blockIndex)
    {
        NpcState state = this.entity.getState();
        AbstractTriggerBlock block = blockIndex < state.triggerTick.blocks.size() ? state.triggerTick.blocks.get(blockIndex) : null;

        if (block instanceof ScriptTriggerBlock)
        {
            ScriptTriggerBlock script = (ScriptTriggerBlock) block;

            script.string = scriptName;
            script.function = functionName;
            script.frequency = frequency;

            this.entity.setState(state, false);
        }
    }

    @Override
    public void addOnTickTrigger(String scriptName, String functionName, int frequency)
    {
        NpcState state = this.entity.getState();
        ScriptTriggerBlock block = new ScriptTriggerBlock();

        block.string = scriptName;
        block.function = functionName;
        block.frequency = frequency;

        state.triggerTick.blocks.add(block);
        this.entity.setState(state, false);
    }

    @Override
    public void clearOnTickTriggers()
    {
        NpcState state = this.entity.getState();

        state.triggerTick.blocks.clear();

        this.entity.setState(state, false);
    }

    @Override
    public void setOnInteractTrigger(String scriptName, String functionName, int blockIndex)
    {
        NpcState state = this.entity.getState();
        AbstractTriggerBlock block = blockIndex < state.triggerInteract.blocks.size() ? state.triggerInteract.blocks.get(blockIndex) : null;

        if (block instanceof ScriptTriggerBlock)
        {
            ScriptTriggerBlock script = (ScriptTriggerBlock) block;

            script.string = scriptName;
            script.function = functionName;

            this.entity.setState(state, false);
        }
    }

    @Override
    public void addOnInteractTrigger(String scriptName, String functionName)
    {
        NpcState state = this.entity.getState();
        ScriptTriggerBlock block = new ScriptTriggerBlock();

        block.string = scriptName;
        block.function = functionName;

        state.triggerInteract.blocks.add(block);
        this.entity.setState(state, false);
    }

    @Override
    public void clearOnInteractTriggers()
    {
        NpcState state = this.entity.getState();

        state.triggerInteract.blocks.clear();

        this.entity.setState(state, false);
    }

    @Override
    public void setPatrol(int x, int y, int z, String scriptName, String functionName, int patrolIndex)
    {
        NpcState state = this.entity.getState();

        if (patrolIndex >= 0)
        {
            if (patrolIndex < state.patrol.size())
            {
                state.patrol.set(patrolIndex, new BlockPos(x, y, z));
            }

            if (patrolIndex < state.patrolTriggers.size())
            {
                Trigger trigger = new Trigger();
                ScriptTriggerBlock block = new ScriptTriggerBlock();

                block.string = scriptName;
                block.function = functionName;

                trigger.blocks.add(block);
                state.patrolTriggers.set(patrolIndex, trigger);
            }
        }

        this.entity.setState(state, false);
    }

    @Override
    public void addPatrol(int x, int y, int z, String scriptName, String functionName)
    {
        NpcState state = this.entity.getState();

        state.follow = "";
        state.patrol.add(new BlockPos(x, y, z));

        Trigger trigger = new Trigger();
        ScriptTriggerBlock block = new ScriptTriggerBlock();

        block.string = scriptName;
        block.function = functionName;

        trigger.blocks.add(block);
        state.patrolTriggers.add(trigger);

        this.entity.setState(state, false);
    }

    @Override
    public void clearPatrolPoints()
    {
        NpcState state = this.entity.getState();

        state.patrol.clear();

        this.entity.setState(state, false);
    }

    @Override
    public String getFaction()
    {
        return this.entity.getState().faction;
    }

    @Override
    public void setFaction(String factionName)
    {
        this.entity.getState().faction = factionName;
    }
}