package mchorse.mappet.api.triggers.blocks;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

public class ScriptTriggerBlock extends DataTriggerBlock
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
    public String stringify()
    {
        if (!this.string.isEmpty() && !this.function.isEmpty())
        {
            return this.string + " (" + TextFormatting.GRAY + this.function + TextFormatting.RESET + ")";
        }

        return super.stringify();
    }

    @Override
    public void trigger(DataContext context)
    {
        if (!this.string.isEmpty())
        {
            try
            {
                DataContext data = this.apply(context);

                Mappet.scripts.execute(this.string, this.function.trim(), data);

                if (!context.isCanceled())
                {
                    context.cancel(data.isCanceled());
                }
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