package mchorse.mappet.api.conditions.blocks;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.conditions.utils.Comparison;
import mchorse.mappet.api.states.States;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.capabilities.character.Character;
import mchorse.mappet.utils.EnumUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public abstract class PropertyBlock extends AbstractBlock
{
    public String id = "";
    public Comparison comparison = Comparison.EQUALS;

    /* 0 = global
     * 1 = subject
     * 2 = object
     */
    public int target;
    public double value;

    protected States getStates(DataContext context)
    {
        if (this.target == 1 && context.subject instanceof EntityPlayer)
        {
            return Character.get((EntityPlayer) context.subject).getStates();
        }
        else if (this.target == 2 && context.object instanceof EntityPlayer)
        {
            return Character.get((EntityPlayer) context.object).getStates();
        }

        return Mappet.states;
    }

    @Override
    public void serializeNBT(NBTTagCompound tag)
    {
        tag.setString("Id", this.id);
        tag.setInteger("Target", this.target);
        tag.setInteger("Comparison", this.comparison.ordinal());
        tag.setDouble("Value", this.value);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.id = tag.getString("Id");
        this.target = tag.getInteger("Target");
        this.comparison = EnumUtils.getValue(tag.getInteger("Comparison"), Comparison.values(), Comparison.EQUALS);
        this.value = tag.getDouble("Value");
    }
}
