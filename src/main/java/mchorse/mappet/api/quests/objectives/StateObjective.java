package mchorse.mappet.api.quests.objectives;

import mchorse.mappet.api.conditions.Checker;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class StateObjective extends AbstractObjective
{
    public Checker expression = new Checker();
    private boolean result;

    @Override
    public boolean isComplete(EntityPlayer player)
    {
        return this.result;
    }

    public boolean updateValue(EntityPlayer player)
    {
        boolean result = this.result;

        this.result = this.expression.check(new DataContext(player));

        return this.result != result;
    }

    @Override
    public void complete(EntityPlayer player)
    {}

    @Override
    public String stringify(EntityPlayer player)
    {
        return this.message;
    }

    @Override
    public String getType()
    {
        return "state";
    }

    @Override
    public NBTTagCompound partialSerializeNBT()
    {
        NBTTagCompound tag = new NBTTagCompound();

        if (this.result)
        {
            tag.setBoolean("Result", this.result);
        }

        return tag;
    }

    @Override
    public void partialDeserializeNBT(NBTTagCompound tag)
    {
        if (tag.hasKey("Result"))
        {
            this.result = tag.getBoolean("Result");
        }
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        tag.setTag("Expression", this.expression.serializeNBT());
        tag.setBoolean("Result", this.result);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.expression.deserializeNBT(tag.getTag("Expression"));
        this.result = tag.getBoolean("Result");
    }
}