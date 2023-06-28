package mchorse.mappet.api.scripts.code.entities;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.api.scripts.code.mappet.triggers.MappetTrigger;
import mchorse.mappet.api.scripts.user.data.ScriptVector;
import mchorse.mappet.api.scripts.user.entities.IScriptNpc;
import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.api.triggers.blocks.AbstractTriggerBlock;
import mchorse.mappet.api.triggers.blocks.ScriptTriggerBlock;
import mchorse.mappet.entities.EntityNpc;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

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
        return this.entity.getState().stateName.get();
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

        state.follow.set(target);

        this.entity.setState(state, false);
    }

    @Override
    public String getFaction()
    {
        return this.entity.getState().faction.get();
    }

    @Override
    public void setCanBeSteered(boolean enabled)
    {
        NpcState state = this.entity.getState();
        state.canBeSteered.set(enabled);
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean canBeSteered()
    {
        return this.entity.getState().canBeSteered.get();
    }

    @Override
    public void setSteeringOffset(int index, float x, float y, float z) {
        NpcState state = this.entity.getState();
        if (index >= 0 && index < state.steeringOffset.size()) {
            state.steeringOffset.set(index, new BlockPos(x, y, z));
        } else {
            throw new IndexOutOfBoundsException("Invalid index: " + index);
        }
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public void addSteeringOffset(float x, float y, float z) {
        NpcState state = this.entity.getState();
        state.steeringOffset.add(new BlockPos(x, y, z));
        this.entity.sendNpcStateChangePacket();
    }


    @Override
    public List<ScriptVector> getSteeringOffsets() {
        NpcState state = this.entity.getState();
        List<ScriptVector> steeringOffsets = new ArrayList<>();
        for (BlockPos pos : state.steeringOffset) {
            steeringOffsets.add(new ScriptVector(pos.getX(), pos.getY(), pos.getZ()));
        }
        return steeringOffsets;
    }

    @Override
    public void setNpcSpeed(float speed)
    {
        NpcState state = this.entity.getState();
        state.speed.set(speed);
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public float getNpcSpeed()
    {
        return this.entity.getState().speed.get();
    }

    @Override
    public void setJumpPower(float jumpHeight)
    {
        NpcState state = this.entity.getState();
        state.jumpPower.set(jumpHeight);
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public float getJumpPower()
    {
        return this.entity.getState().jumpPower.get();
    }

    @Override
    public void setInvincible(boolean invincible)
    {
        NpcState state = this.entity.getState();
        state.invincible.set(invincible);
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean isInvincible()
    {
        return this.entity.getState().invincible.get();
    }

    @Override
    public void setCanSwim(boolean canSwim)
    {
        NpcState state = this.entity.getState();
        state.canSwim.set(canSwim);
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean canSwim()
    {
        return this.entity.getState().canSwim.get();
    }

    @Override
    public void setImmovable(boolean immovable)
    {
        NpcState state = this.entity.getState();
        state.immovable.set(immovable);
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean isImmovable()
    {
        return this.entity.getState().immovable.get();
    }

    @Override
    public void setShadowSize(float size)
    {
        NpcState state = this.entity.getState();
        state.shadowSize.set(size);
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public float getShadowSize()
    {
        return this.entity.getState().shadowSize.get();
    }

    @Override
    public float setXpValue(int xp)
    {
        NpcState state = this.entity.getState();
        state.xp.set(xp);
        this.entity.sendNpcStateChangePacket();
        return xp;
    }

    @Override
    public int getXpValue()
    {
        return this.entity.getState().xp.get();
    }

    @Override
    public float getPathDistance()
    {
        NpcState state = this.entity.getState();
        return state.pathDistance.get();
    }

    @Override
    public void setPathDistance(float sightRadius)
    {
        NpcState state = this.entity.getState();
        state.pathDistance.set(sightRadius);
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public void setAttackRange(float sightDistance)
    {
        NpcState state = this.entity.getState();
        state.sightDistance.set(sightDistance);
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public float getAttackRange()
    {
        return this.entity.getState().sightDistance.get();
    }

    @Override
    public void setKillable(boolean killable)
    {
        NpcState state = this.entity.getState();
        state.killable.set(killable);
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean isKillable()
    {
        return this.entity.getState().killable.get();
    }

    @Override
    public boolean canGetBurned()
    {
        return this.entity.getState().canGetBurned.get();
    }

    @Override
    public void canGetBurned(boolean canGetBurned)
    {
        NpcState state = this.entity.getState();
        state.canGetBurned.set(canGetBurned);
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean canFallDamage()
    {
        return this.entity.getState().canFallDamage.get();
    }

    @Override
    public void canFallDamage(boolean canFallDamage)
    {
        NpcState state = this.entity.getState();
        state.canFallDamage.set(canFallDamage);
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public float getDamage()
    {
        return this.entity.getState().damage.get();
    }

    @Override
    public void setDamage(float damage)
    {
        NpcState state = this.entity.getState();
        state.damage.set(damage);
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public int getDamageDelay()
    {
        return this.entity.getState().damageDelay.get();
    }

    @Override
    public void setDamageDelay(int damageDelay)
    {
        NpcState state = this.entity.getState();
        state.damageDelay.set(damageDelay);
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean doesWander()
    {
        return this.entity.getState().wander.get();
    }

    @Override
    public void setWander(boolean wander)
    {
        NpcState state = this.entity.getState();
        state.wander.set(wander);
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean doesLookAround()
    {
        return this.entity.getState().lookAround.get();
    }

    @Override
    public void setLookAround(boolean lookAround)
    {
        NpcState state = this.entity.getState();
        state.lookAround.set(lookAround);
        this.entity.sendNpcStateChangePacket();
    }

    @Override
    public boolean doesLookAtPlayer()
    {
        return this.entity.getState().lookAtPlayer.get();
    }

    @Override
    public void setLookAtPlayer(boolean lookAtPlayer)
    {
        NpcState state = this.entity.getState();
        state.lookAtPlayer.set(lookAtPlayer);
        this.entity.sendNpcStateChangePacket();
    }

    /* Triggers */

    @Override
    public MappetTrigger getOnInitializeTrigger()
    {
        return new MappetTrigger(this.entity.getState().triggerInitialize);
    }

    @Override
    public MappetTrigger getOnInteractTrigger()
    {
        return new MappetTrigger(this.entity.getState().triggerInteract);
    }

    @Override
    public MappetTrigger getOnDamagedTrigger()
    {
        return new MappetTrigger(this.entity.getState().triggerDamaged);
    }

    @Override
    public MappetTrigger getOnDeathTrigger()
    {
        return new MappetTrigger(this.entity.getState().triggerDied);
    }

    @Override
    public MappetTrigger getOnTickTrigger()
    {
        return new MappetTrigger(this.entity.getState().triggerTick);
    }

    @Override
    public MappetTrigger getOnTargetTrigger()
    {
        return new MappetTrigger(this.entity.getState().triggerTarget);
    }

    @Override
    public MappetTrigger getOnCollisionTrigger()
    {
        return new MappetTrigger(this.entity.getState().triggerEntityCollision);
    }

    @Override
    public MappetTrigger getOnRespawnTrigger()
    {
        return new MappetTrigger(this.entity.getState().triggerRespawn);
    }

    /* Patrol points */

    @Override
    public void addPatrolPoint(int x, int y, int z, MappetTrigger trigger)
    {
        NpcState state = this.entity.getState();

        state.follow.set("");
        state.patrol.add(new BlockPos(x, y, z));
        state.patrolTriggers.add(trigger.getMinecraftTrigger());

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
    public void removePatrolPoint(int index)
    {
        NpcState state = this.entity.getState();

        if (index < state.patrol.size())
        {
            state.patrol.remove(index);
            state.patrolTriggers.remove(index);
        }

        this.entity.setState(state, false);
    }

    @Override
    public void removePatrolPoint(int x, int y, int z)
    {
        NpcState state = this.entity.getState();

        state.patrol.stream().filter(p -> p.getX() == x && p.getY() == y && p.getZ() == z).forEach(p -> {
            int index = state.patrol.indexOf(p);

            state.patrol.remove(index);
            state.patrolTriggers.remove(index);
        });

        this.entity.setState(state, false);
    }
}