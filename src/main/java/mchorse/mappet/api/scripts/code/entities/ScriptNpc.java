package mchorse.mappet.api.scripts.code.entities;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.api.scripts.user.data.ScriptVector;
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
    public void setCanBeSteered(boolean enabled)
    {
        NpcState state = this.entity.getState();
        state.canBeSteered = enabled;
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean canBeSteered()
    {
        return this.entity.getState().canBeSteered;
    }

    @Override
    public void setSteeringOffset(float x, float y, float z)
    {
        NpcState state = this.entity.getState();
        state.steeringXOffset = x;
        state.steeringYOffset = y;
        state.steeringZOffset = z;
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public ScriptVector getSteeringOffset()
    {
        NpcState state = this.entity.getState();
        return new ScriptVector(state.steeringXOffset, state.steeringYOffset, state.steeringZOffset);
    }

    @Override
    public void setNpcSpeed(float speed)
    {
        NpcState state = this.entity.getState();
        state.speed = speed;
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public float getNpcSpeed()
    {
        return this.entity.getState().speed;
    }

    @Override
    public void setjumpPower(float jumpHeight)
    {
        NpcState state = this.entity.getState();
        state.jumpPower = jumpHeight;
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public float getjumpPower()
    {
        return this.entity.getState().jumpPower;
    }

    @Override
    public void setInvincible(boolean invencible)
    {
        NpcState state = this.entity.getState();
        state.invincible = invencible;
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean isInvincible()
    {
        return this.entity.getState().invincible;
    }

    @Override
    public void setCanSwim(boolean canSwim)
    {
        NpcState state = this.entity.getState();
        state.canSwim = canSwim;
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean canSwim()
    {
        return this.entity.getState().canSwim;
    }

    @Override
    public void setImmovable(boolean immovable)
    {
        NpcState state = this.entity.getState();
        state.immovable = immovable;
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean isImmovable()
    {
        return this.entity.getState().immovable;
    }

    @Override
    public void setShadowSize(float size)
    {
        NpcState state = this.entity.getState();
        state.shadowSize = size;
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public float getShadowSize()
    {
        return this.entity.getState().shadowSize;
    }

    @Override
    public float setXpValue(int xp)
    {
        NpcState state = this.entity.getState();
        state.xp = xp;
        this.entity.sendNpcStateChangePacket();
        return xp;
    }

    @Override
    public int getXpValue()
    {
        return this.entity.getState().xp;
    }

    @Override
    public float getPathDistance()
    {
        NpcState state = this.entity.getState();
        return state.pathDistance;
    }

    @Override
    public void setPathDistance(float sightRadius)
    {
        NpcState state = this.entity.getState();
        state.pathDistance = sightRadius;
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public void setAttackRange(float sightDistance){
        NpcState state = this.entity.getState();
        state.sightDistance = sightDistance;
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public float getAttackRange(){
        return this.entity.getState().sightDistance;
    }

    @Override
    public void setKillable(boolean killable)
    {
        NpcState state = this.entity.getState();
        state.killable = killable;
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean isKillable()
    {
        return this.entity.getState().killable;
    }

    @Override
    public boolean canGetBurned()
    {
        return this.entity.getState().canGetBurned;
    }

    @Override
    public void canGetBurned(boolean canGetBurned)
    {
        NpcState state = this.entity.getState();
        state.canGetBurned = canGetBurned;
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean canFallDamage()
    {
        return this.entity.getState().canFallDamage;
    }

    @Override
    public void canFallDamage(boolean canFallDamage)
    {
        NpcState state = this.entity.getState();
        state.canFallDamage = canFallDamage;
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public float getDamage()
    {
        return this.entity.getState().damage;
    }

    @Override
    public void setDamage(float damage)
    {
        NpcState state = this.entity.getState();
        state.damage = damage;
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public int getDamageDelay()
    {
        return this.entity.getState().damageDelay;
    }

    @Override
    public void setDamageDelay(int damageDelay)
    {
        NpcState state = this.entity.getState();
        state.damageDelay = damageDelay;
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean doesWander()
    {
        return this.entity.getState().wander;
    }

    @Override
    public void setWander(boolean wander)
    {
        NpcState state = this.entity.getState();
        state.wander = wander;
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean doesLookAround()
    {
        return this.entity.getState().lookAround;
    }

    @Override
    public void setLookAround(boolean lookAround)
    {
        NpcState state = this.entity.getState();
        state.lookAround = lookAround;
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean doesLookAtPlayer()
    {
        return this.entity.getState().lookAtPlayer;
    }

    @Override
    public void setLookAtPlayer(boolean lookAtPlayer)
    {
        NpcState state = this.entity.getState();
        state.lookAtPlayer = lookAtPlayer;
        this.entity.sendNpcStateChangePacket();
    }
}