package mchorse.mappet.api.triggers.blocks;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.nbt.NBTTagCompound;

public class ScriptTriggerBlock extends StringTriggerBlock
{
    public String function = "";

    public ScriptTriggerBlock()
    {
        super();
    }

    public ScriptTriggerBlock(String string, String function)
    {
        super(string);

        this.function = function;
    }

    @Override
    public void trigger(DataContext context)
    {
        if (!this.string.isEmpty())
        {
            try
            {
                Mappet.scripts.execute(this.string, this.function.isEmpty() ? "main" : this.function.trim(), context);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected String getKey()
    {
        return "Script";
    }

    @Override
    protected void serializeNBT(NBTTagCompound tag)
    {
        super.serializeNBT(tag);

        tag.setString("Function", this.function);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.function = tag.getString("Function");
    }
}