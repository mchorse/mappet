package mchorse.mappet.api.quests.objectives;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class KillObjective implements IObjective
{
    public ResourceLocation entity = new ResourceLocation("");
    public int count;
    public int killed;

    public KillObjective()
    {}

    public KillObjective(ResourceLocation entity, int quantity)
    {
        this.entity = entity;
        this.count = quantity;
    }

    public void playerKilled(EntityPlayer player, Entity mob)
    {
        if (this.entity.equals(EntityList.getKey(mob)))
        {
            this.killed += 1;
        }
    }

    @Override
    public boolean isComplete(EntityPlayer player)
    {
        return this.killed >= this.count;
    }

    @Override
    public void complete(EntityPlayer player)
    {}

    @Override
    public IObjective copy()
    {
        return new KillObjective(this.entity, this.count);
    }

    @Override
    public String stringify(EntityPlayer player)
    {
        return "Kill " + EntityList.getTranslationName(this.entity) + " (" + Math.min(this.killed, this.count) + "/" + this.count + ")";
    }

    @Override
    public String getType()
    {
        return "kill";
    }

    @Override
    public NBTTagCompound partialSerializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("Killed", this.killed);

        return tag;
    }

    @Override
    public void partialDeserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Killed"))
        {
            this.killed = tag.getInteger("Killed");
        }
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setString("Entity", this.entity.toString());
        tag.setInteger("Count", this.count);
        tag.setInteger("Killed", this.killed);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.entity = new ResourceLocation(tag.getString("Entity"));
        this.count = tag.getInteger("Count");
        this.killed = tag.getInteger("Killed");
    }
}