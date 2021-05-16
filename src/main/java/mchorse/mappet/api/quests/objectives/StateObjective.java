package mchorse.mappet.api.quests.objectives;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.expressions.ExpressionManager;
import mchorse.mclib.math.IValue;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class StateObjective extends AbstractObjective
{
    public String expression = "";
    private IValue value;
    private boolean result;

    @Override
    public boolean isComplete(EntityPlayer player)
    {
        return this.result;
    }

    public boolean updateValue(EntityPlayer player)
    {
        if (this.expression.isEmpty())
        {
            return false;
        }

        if (this.value == null)
        {
            this.value = Mappet.expressions.evaluate(this.expression, ExpressionManager.ZERO);
        }

        Mappet.expressions.set(player);
        boolean result = this.result;

        this.result = this.value.booleanValue();

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

        tag.setString("Expression", this.expression);
        tag.setBoolean("Result", this.result);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.expression = tag.getString("Expression");
        this.result = tag.getBoolean("Result");
    }
}