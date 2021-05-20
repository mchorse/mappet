package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.conditions.utils.Comparison;
import mchorse.mappet.api.conditions.utils.Target;
import mchorse.mappet.api.states.States;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.utils.EnumUtils;
import net.minecraft.command.EntitySelector;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public abstract class PropertyBlock extends AbstractBlock
{
    public String id = "";
    public Comparison comparison = Comparison.EQUALS;
    public Target target = this.getDefaultTarget();
    public String selector = "";
    public double value;

    protected States getStates(DataContext context)
    {
        if (this.target == Target.SUBJECT && context.subject instanceof EntityPlayer)
        {
            return Character.get((EntityPlayer) context.subject).getStates();
        }
        else if (this.target == Target.OBJECT && context.object instanceof EntityPlayer)
        {
            return Character.get((EntityPlayer) context.object).getStates();
        }
        else if (this.target == Target.SELECTOR)
        {
            try
            {
                EntityPlayer player = EntitySelector.matchOnePlayer(context.getSender(), this.selector);

                if (player != null)
                {
                    return Character.get(player).getStates();
                }
            }
            catch (Exception e)
            {}

            return null;
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
        tag.setString("Id", this.id);
        tag.setInteger("Target", this.target.ordinal());
        tag.setString("Selector", this.selector);
        tag.setInteger("Comparison", this.comparison.ordinal());
        tag.setDouble("Value", this.value);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.id = tag.getString("Id");
        this.target = EnumUtils.getValue(tag.getInteger("Target"), Target.values(), this.getDefaultTarget());
        this.selector = tag.getString("Selector");
        this.comparison = EnumUtils.getValue(tag.getInteger("Comparison"), Comparison.values(), Comparison.EQUALS);
        this.value = tag.getDouble("Value");
    }
}
