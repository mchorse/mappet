package mchorse.mappet.api.scripts.code.entities;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.npcs.Npc;
import mchorse.mappet.api.npcs.NpcState;
import mchorse.mappet.api.scripts.code.ScriptFactory;
import mchorse.mappet.api.scripts.code.nbt.ScriptNBTCompound;
import mchorse.mappet.api.scripts.user.entities.IScriptNpc;
import mchorse.mappet.api.scripts.user.nbt.INBTCompound;
import mchorse.mappet.api.scripts.user.nbt.INBTList;
import mchorse.mappet.entities.EntityNpc;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.nbt.NBTTagCompound;

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
        INBTCompound fullData = new ScriptNBTCompound(this.entity.writeToNBT(new NBTTagCompound()));
        return fullData.getCompound("State").getString("StateName");
    }

    @Override
    public void setNpcState(String stateId)
    {
        this.entity.sendMorph();
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
            if(!npc.serializeNBT().getString("StateName").equals("default")){
                this.entity.setStringInData("StateName", stateId);

            }
        }
        this.entity.sendMorph();
    }

    @Override
    public void canPickUpLoot(boolean canPickUpLoot)
    {
        INBTCompound fullData = new ScriptNBTCompound(this.entity.writeToNBT(new NBTTagCompound()));
        fullData.setByte("CanPickUpLoot", (byte) (canPickUpLoot ? 1 : 0));
        this.entity.readFromNBT(fullData.getNBTTagCompound());
    }

    @Override
    public void follow(String target)
    {
        INBTCompound fullData = new ScriptNBTCompound(this.entity.writeToNBT(new NBTTagCompound()));
        fullData.getCompound("State").setString("Follow", target);
        this.entity.readFromNBT(fullData.getNBTTagCompound());
    }

    @Override
    public void setOnTickTrigger(String scriptName, String functionName, int frequency, int blockNumber)
    {
        INBTCompound fullData = new ScriptNBTCompound(this.entity.writeToNBT(new NBTTagCompound()));
        fullData.getCompound("State").getCompound("TriggerTick").getList("Blocks").getCompound(blockNumber).setString("Script", scriptName);
        fullData.getCompound("State").getCompound("TriggerTick").getList("Blocks").getCompound(blockNumber).setString("Function", functionName);
        fullData.getCompound("State").getCompound("TriggerTick").getList("Blocks").getCompound(blockNumber).setInt("Frequency", frequency);
        this.entity.readFromNBT(fullData.getNBTTagCompound());
    }

    @Override
    public void addOnTickTrigger(String scriptName, String functionName, int frequency){
        ScriptFactory factory = new ScriptFactory();
        INBTCompound fullData = new ScriptNBTCompound(this.entity.writeToNBT(new NBTTagCompound()));
        INBTList blocks = fullData.getCompound("State").getCompound("TriggerTick").getList("Blocks");
        blocks.addCompound(factory.createCompound());
        blocks.getCompound(blocks.size() - 1).setString("Script", scriptName);
        blocks.getCompound(blocks.size() - 1).setString("Type", "script");
        blocks.getCompound(blocks.size() - 1).setString("Function", functionName);
        blocks.getCompound(blocks.size() - 1).setString("CustomData", "");
        blocks.getCompound(blocks.size() - 1).setInt("Frequency", frequency);
        this.entity.readFromNBT(fullData.getNBTTagCompound());
    }

    @Override
    public void clearOnTickTriggers(){
        INBTCompound fullData = new ScriptNBTCompound(this.entity.writeToNBT(new NBTTagCompound()));
        if (!fullData.getCompound("State").getCompound("TriggerTick").getList("Blocks").has(0)) {return;}
        INBTList blocks = fullData.getCompound("State").getCompound("TriggerTick").getList("Blocks");
        for(int i = blocks.size()-1; i >= 0; i--){
            blocks.remove(i);
            this.entity.readFromNBT(fullData.getNBTTagCompound());
        }
    }


    @Override
    public void setOnInteractTrigger(String scriptName, String functionName, int blockNumber)
    {
        INBTCompound fullData = new ScriptNBTCompound(this.entity.writeToNBT(new NBTTagCompound()));
        fullData.getCompound("State").getCompound("TriggerInteract").getList("Blocks").getCompound(blockNumber).setString("Script", scriptName);
        fullData.getCompound("State").getCompound("TriggerInteract").getList("Blocks").getCompound(blockNumber).setString("Function", functionName);
        this.entity.readFromNBT(fullData.getNBTTagCompound());
    }

    @Override
    public void addOnInteractTrigger(String scriptName, String functionName)
    {
        INBTCompound fullData = new ScriptNBTCompound(this.entity.writeToNBT(new NBTTagCompound()));
        ScriptFactory factory = new ScriptFactory();
        INBTCompound triggerData = factory.createCompound();
        triggerData.setString("Function", functionName);
        triggerData.setString("Script", scriptName);
        triggerData.setString("Type", "script");
        triggerData.setString("CustomData", "");
        triggerData.setInt("Frequency", 1);

        fullData.getCompound("State").getCompound("TriggerInteract").getList("Blocks").addCompound(triggerData);

        this.entity.readFromNBT(fullData.getNBTTagCompound());
    }

    @Override
    public void clearOnInteractTriggers(){
        INBTCompound fullData = new ScriptNBTCompound(this.entity.writeToNBT(new NBTTagCompound()));
        if (!fullData.getCompound("State").getCompound("TriggerInteract").getList("Blocks").has(0)) {return;}
        INBTList blocks = fullData.getCompound("State").getCompound("TriggerInteract").getList("Blocks");
        for(int i = blocks.size()-1; i >= 0; i--){
            blocks.remove(i);
            this.entity.readFromNBT(fullData.getNBTTagCompound());
        }
    }

    @Override
    public void setPatrol(int x, int y, int z, String scriptName, String functionName)
    {
        INBTCompound fullData = new ScriptNBTCompound(this.entity.writeToNBT(new NBTTagCompound()));
        fullData.getCompound("State").setString("Follow", "");

        ScriptFactory factory = new ScriptFactory();
        INBTList patrolPoints = factory.createList();
        patrolPoints.addInt(x);
        patrolPoints.addInt(y);
        patrolPoints.addInt(z);
        fullData.getCompound("State").getList("Patrol").setList(0, patrolPoints);
        fullData.getCompound("State").getList("PatrolTriggers").getCompound(0).getList("Blocks").getCompound(0).setString("Function", functionName);
        fullData.getCompound("State").getList("PatrolTriggers").getCompound(0).getList("Blocks").getCompound(0).setString("Script", scriptName);
        this.entity.readFromNBT(fullData.getNBTTagCompound());
    }

    @Override
    public void addPatrol(int x, int y, int z, String scriptName, String functionName)
    {
        INBTCompound fullData = new ScriptNBTCompound(this.entity.writeToNBT(new NBTTagCompound()));

        //add patrol point
        ScriptFactory factory = new ScriptFactory();
        INBTList patrolPoints = factory.createList();
        patrolPoints.addInt(x);
        patrolPoints.addInt(y);
        patrolPoints.addInt(z);
        fullData.getCompound("State").getList("Patrol").addList(patrolPoints);

        //add patrol trigger
        INBTCompound triggerData = factory.createCompound();
        triggerData.setString("Function", functionName);
        triggerData.setString("Script", scriptName);
        triggerData.setString("Type", "script");
        triggerData.setString("CustomData", "");
        triggerData.setInt("Frequency", 1);

        INBTList triggerBlock = factory.createList();
        triggerBlock.addCompound(triggerData);

        fullData.getCompound("State").getList("PatrolTriggers").addCompound(factory.createCompound());
        fullData.getCompound("State").getList("PatrolTriggers").getCompound(fullData.getCompound("State").getList("PatrolTriggers").size() - 1).setList("Blocks", triggerBlock);

        this.entity.readFromNBT(fullData.getNBTTagCompound());
    }

    @Override
    public void clearPatrolPoints(){
        INBTCompound fullData = new ScriptNBTCompound(this.entity.writeToNBT(new NBTTagCompound()));
        if (!fullData.getCompound("State").getList("Patrol").has(0)) {return;}
        int sizeIndex = fullData.getCompound("State").getList("Patrol").size() - 1;

        for (int i = sizeIndex; i >= 0; i--) {
            fullData.getCompound("State").getList("Patrol").remove(i);
            //fullData.getCompound("State").getList("PatrolTriggers").getCompound(i).getList("Blocks").remove(0);
            this.entity.readFromNBT(fullData.getNBTTagCompound());
        }
    }

    @Override
    public String getFaction()
    {
        INBTCompound fullData = new ScriptNBTCompound(this.entity.writeToNBT(new NBTTagCompound()));
        return fullData.getCompound("State").getString("Faction");
    }
}