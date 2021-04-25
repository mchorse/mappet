package mchorse.mappet.api.factions;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.HashMap;
import java.util.Map;

public class Faction implements INBTSerializable<NBTTagCompound>
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
    public Map<String, FactionRelation> relations = new HashMap<String, FactionRelation>();

    public Faction()
    {}

    public Faction(String id)
    {
        this.id = id;
    }

    public String getId()
    {
        return this.id;
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

        for (Map.Entry<String, FactionRelation> entry : this.relations.entrySet())
        {
            relations.setTag(entry.getKey(), entry.getValue().serializeNBT());
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
                FactionRelation relation = new FactionRelation();

                relation.deserializeNBT(relations.getCompoundTag(key));
                this.relations.put(key, relation);
            }
        }
    }
}