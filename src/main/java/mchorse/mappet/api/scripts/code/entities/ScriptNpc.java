package mchorse.mappet.api.scripts.code.entities;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.api.scripts.user.entities.IScriptNpc;
import mchorse.mappet.entities.EntityNpc;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;

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
        this.entity.sendMorph();

        return true;
    }

    @Override
    public String getNpcState()
    {
        return this.entity.getState().id;
    }

    @Override
    public void setNpcState(String id)
    {
        Npc npc = Mappet.npcs.load(this.entity.getId());
        NpcState state = npc.states.get(id);
        this.entity.setState(state, true);
    }
}