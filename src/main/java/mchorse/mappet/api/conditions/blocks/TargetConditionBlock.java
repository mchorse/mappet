package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.conditions.utils.Target;
import mchorse.mappet.api.states.States;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.capabilities.character.ICharacter;
import mchorse.mappet.utils.EntityUtils;
import mchorse.mappet.utils.EnumUtils;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public abstract class TargetConditionBlock extends AbstractConditionBlock
{
    public String id = "";
    public Target target = this.getDefaultTarget();
    public String selector = "";

    protected EntityPlayer getPlayer(DataContext context)
    {
        if (this.target == Target.SUBJECT && context.subject instanceof EntityPlayer)
        {
            return (EntityPlayer) context.subject;
        }
        else if (this.target == Target.OBJECT && context.object instanceof EntityPlayer)
        {
            return (EntityPlayer) context.object;
        }
        else if (this.target == Target.PLAYER)
        {
            return context.getPlayer();
        }
        else if (this.target == Target.SELECTOR)
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

    protected Entity getEntity(DataContext context)
    {
        if (this.target == Target.SUBJECT && context.subject != null)
        {
            return context.subject;
        }
        else if (this.target == Target.OBJECT && context.object != null)
        {
            return context.object;
        }
        else if (this.target == Target.PLAYER)
        {
            return context.getPlayer();
        }
        else if (this.target == Target.NPC)
        {
            return context.getNPC();
        }
        else if (this.target == Target.SELECTOR)
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

    protected ICharacter getCharacter(DataContext context)
    {
        return Character.get(this.getPlayer(context));
    }

    protected States getStates(DataContext context)
    {
        if (this.target != Target.GLOBAL)
        {
            return EntityUtils.getStates(this.getEntity(context));
        }

        return Mappet.states;
    }

    protected Target getDefaultTarget()
    {
        return Target.GLOBAL;
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        tag.setString("Id", this.id.trim());
        tag.setInteger("Target", this.target.ordinal());
        tag.setString("Selector", this.selector);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.id = tag.getString("Id");
        this.target = EnumUtils.getValue(tag.getInteger("Target"), Target.values(), this.getDefaultTarget());
        this.selector = tag.getString("Selector");
    }
}
