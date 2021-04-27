package mchorse.mappet.api.factions;

import mchorse.mappet.api.states.States;
import mchorse.mappet.api.utils.IID;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class Faction implements INBTSerializable<NBTTagCompound>, IID
{
    private String id;

    /**
     * The display name of the faction
     */
    public String title = "";

    /**
     * Color of the faction
     */
    public int color = 0xffffff;

    /**
     * Default score upon player joining the faction
     */
    public int score = 500;

    /**
     * Default attitude towards players who has no factions
     */
    public FactionAttitude playerAttitude = FactionAttitude.PASSIVE;

    /**
     * Default attitude towards NPCs or any other entities that can
     * join factions who has no factions
     */
    public FactionAttitude othersAttitude = FactionAttitude.PASSIVE;

    /**
     * It's own relation toward its own
     */
    public FactionRelation ownRelation = new FactionRelation();

    /**
     * Relations to other factions
     */
    public Map<String, FactionAttitude> relations = new HashMap<String, FactionAttitude>();

    public Faction()
    {}

    public Faction(String id)
    {
        this.setId(id);
    }

    @Override
    public String getId()
    {
        return this.id;
    }

    @Override
    public void setId(String id)
    {
        this.id = id;
    }

    public FactionAttitude get(States states)
    {
        if (states.hasFaction(this.getId()))
        {
            return this.ownRelation.getAttitude(states.getFactionScore(this.getId()));
        }

        for (String key : this.relations.keySet())
        {
            if (states.hasFaction(key))
            {
                return this.relations.get(key);
            }
        }

        return this.playerAttitude;
    }

    public FactionAttitude get(String faction)
    {
        FactionAttitude attitude = this.relations.get(faction);

        return attitude == null ? this.othersAttitude : attitude;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setString("Title", this.title);
        tag.setInteger("Color", this.color);
        tag.setInteger("DefaultScore", this.score);
        tag.setString("PlayerAttitude", this.playerAttitude.name());
        tag.setString("OthersAttitude", this.othersAttitude.name());
        tag.setTag("OwnRelation", this.ownRelation.serializeNBT());

        NBTTagCompound relations = new NBTTagCompound();

        for (Map.Entry<String, FactionAttitude> entry : this.relations.entrySet())
        {
            relations.setString(entry.getKey(), entry.getValue().name());
        }

        if (relations.getSize() > 0)
        {
            tag.setTag("Relations", relations);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Title")) this.title = tag.getString("Title");
        if (tag.hasKey("Color")) this.color = tag.getInteger("Color");
        if (tag.hasKey("DefaultScore")) this.score = tag.getInteger("DefaultScore");
        if (tag.hasKey("PlayerAttitude")) this.playerAttitude = FactionAttitude.get(tag.getString("PlayerAttitude"));
        if (tag.hasKey("OthersAttitude")) this.othersAttitude = FactionAttitude.get(tag.getString("OthersAttitude"));
        if (tag.hasKey("Relations"))
        {
            NBTTagCompound relations = tag.getCompoundTag("Relations");

            for (String key : relations.getKeySet())
            {
                this.relations.put(key, FactionAttitude.get(relations.getString(key)));
            }
        }
    }
}