package mchorse.mappet.api.utils;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.states.States;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.utils.EntityUtils;
import mchorse.mappet.utils.EnumUtils;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.INBTSerializable;

public class Target implements INBTSerializable<NBTTagCompound>
{
    public TargetMode mode;
    public String selector = "";

    private TargetMode defaultMode;

    public Target(TargetMode mode)
    {
        this.mode = this.defaultMode = mode;
    }

    public EntityPlayer getPlayer(DataContext context)
    {
        if (this.mode == TargetMode.SUBJECT && context.subject instanceof EntityPlayer)
        {
            return (EntityPlayer) context.subject;
        }
        else if (this.mode == TargetMode.OBJECT && context.object instanceof EntityPlayer)
        {
            return (EntityPlayer) context.object;
        }
        else if (this.mode == TargetMode.PLAYER)
        {
            return context.getPlayer();
        }
        else if (this.mode == TargetMode.SELECTOR)
        {
            try
            {
                return EntitySelector.matchOnePlayer(context.getSender(), this.selector);
            }
            catch (Exception e)
            {}
        }

        return null;
    }

    public Entity getEntity(DataContext context)
    {
        if (this.mode == TargetMode.SUBJECT && context.subject != null)
        {
            return context.subject;
        }
        else if (this.mode == TargetMode.OBJECT && context.object != null)
        {
            return context.object;
        }
        else if (this.mode == TargetMode.PLAYER)
        {
            return context.getPlayer();
        }
        else if (this.mode == TargetMode.NPC)
        {
            return context.getNpc();
        }
        else if (this.mode == TargetMode.SELECTOR)
        {
            try
            {
                return EntitySelector.matchOneEntity(context.getSender(), this.selector, Entity.class);
            }
            catch (Exception e)
            {}
        }

        return null;
    }

    public ICharacter getCharacter(DataContext context)
    {
        return Character.get(this.getPlayer(context));
    }

    public States getStates(DataContext context)
    {
        if (this.mode != TargetMode.GLOBAL)
        {
            return EntityUtils.getStates(this.getEntity(context));
        }

        return Mappet.states;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        tag.setInteger("Target", this.mode.ordinal());
        tag.setString("Selector", this.selector);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        this.mode = EnumUtils.getValue(tag.getInteger("Target"), TargetMode.values(), this.defaultMode);
        this.selector = tag.getString("Selector");
    }
}