package mchorse.mappet.api.quests.objectives;

import mchorse.mappet.api.conditions.Checker;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class StateObjective extends AbstractObjective
{
    public Checker expression = new Checker();
    private boolean result;
    private String compiledMessage;

    @Override
    public void initiate(EntityPlayer player)
    {
        super.initiate(player);

        if (this.message.contains("${"))
        {
            this.compiledMessage = new DataContext(player).process(this.message);
        }
    }

    @Override
    public boolean isComplete(EntityPlayer player)
    {
        return this.result;
    }

    public boolean updateValue(EntityPlayer player)
    {
        boolean result = this.result;
        DataContext data = new DataContext(player);

        this.result = this.expression.check(data);

        if (this.message.contains("${"))
        {
            this.compiledMessage = data.process(this.message);

            return true;
        }

        return this.result != result;
    }

    @Override
    public void complete(EntityPlayer player)
    {}

    @Override
    public String stringifyObjective(EntityPlayer player)
    {
        return this.compiledMessage == null ? this.message : this.compiledMessage;
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

        if (this.compiledMessage != null)
        {
            tag.setString("CompiledMessage", this.compiledMessage);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.expression.deserializeNBT(tag.getTag("Expression"));
        this.result = tag.getBoolean("Result");

        if (tag.hasKey("CompiledMessage"))
        {
            this.compiledMessage = tag.getString("CompiledMessage");
        }
        else
        {
            this.compiledMessage = null;
        }
    }
}