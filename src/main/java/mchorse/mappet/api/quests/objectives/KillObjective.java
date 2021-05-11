package mchorse.mappet.api.quests.objectives;

import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class KillObjective implements IObjective
{
    public ResourceLocation entity = new ResourceLocation("");
    public NBTTagCompound tag;
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
            if (!this.compareTag(mob))
            {
                return;
            }

            this.killed += 1;
        }
    }

    private boolean compareTag(Entity mob)
    {
        NBTTagCompound tag = mob.writeToNBT(new NBTTagCompound());

        for (String key : this.tag.getKeySet())
        {
            NBTBase tagBase = tag.getTag(key);

            if (tagBase == null || !this.tag.getTag(key).equals(tagBase))
            {
                return false;
            }
        }

        return true;
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
    @SideOnly(Side.CLIENT)
    public String stringify(EntityPlayer player)
    {
        String entity = EntityList.getTranslationName(this.entity);
        int count = Math.min(this.killed, this.count);

        return I18n.format("mappet.gui.quests.objective_kill.string", entity, count, this.count);
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

        if (this.tag != null)
        {
            tag.setTag("Tag", this.tag);
        }

        tag.setInteger("Count", this.count);
        tag.setInteger("Killed", this.killed);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.entity = new ResourceLocation(tag.getString("Entity"));

        if (tag.hasKey("Tag", Constants.NBT.TAG_COMPOUND))
        {
            this.tag = tag.getCompoundTag("Tag");
        }

        this.count = tag.getInteger("Count");
        this.killed = tag.getInteger("Killed");
    }
}