package mchorse.mappet.api.factions;

import mchorse.mappet.api.states.States;
import mchorse.mappet.api.utils.AbstractData;
import mchorse.mappet.api.utils.Checker;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

import java.util.HashMap;
import java.util.Map;

public class Faction extends AbstractData
{
    /**
     * The display name of the faction
     */
    public String title = "";

    /**
     * Enabled condition
     */
    public Checker visible = new Checker(true);

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
        if (faction.equals(this.getId()))
        {
            return FactionAttitude.FRIENDLY;
        }

        FactionAttitude attitude = this.relations.get(faction);

        return attitude == null ? this.othersAttitude : attitude;
    }

    public boolean isVisible(EntityPlayer player)
    {
        return this.visible.check(new DataContext(player));
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setString("Title", this.title);
        tag.setTag("Visible", this.visible.serializeNBT());
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
        if (tag.hasKey("Visible")) this.visible.deserializeNBT(tag.getCompoundTag("Visible"));
        if (tag.hasKey("Color")) this.color = tag.getInteger("Color");
        if (tag.hasKey("DefaultScore")) this.score = tag.getInteger("DefaultScore");
        if (tag.hasKey("PlayerAttitude")) this.playerAttitude = FactionAttitude.get(tag.getString("PlayerAttitude"));
        if (tag.hasKey("OthersAttitude")) this.othersAttitude = FactionAttitude.get(tag.getString("OthersAttitude"));
        if (tag.hasKey("OwnRelation")) this.ownRelation.deserializeNBT(tag.getCompoundTag("OwnRelation"));
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