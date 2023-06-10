package mchorse.mappet.api.triggers.blocks;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.utils.ScriptUtils;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.text.TextFormatting;

import javax.script.ScriptException;

public class ScriptTriggerBlock extends DataTriggerBlock
{
    public String function = "";

    public boolean inline = false;
    public String code = "";

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
    public boolean isEmpty()
    {
        return this.inline ? this.code.isEmpty() : this.string.isEmpty();
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
        if (this.inline)
        {
            try
            {
                Mappet.scripts.eval(ScriptUtils.sanitize(ScriptUtils.getEngineByExtension("js")), this.code, context);
            }
            catch (ScriptException scriptException)
            {
                Mappet.logger.error(scriptException.getMessage());
            }
        }

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
        tag.setBoolean("Inline", this.inline);
        tag.setString("Code", this.code);
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        this.function = tag.getString("Function");

        if (tag.hasKey("Inline"))
        {
            this.inline = tag.getBoolean("Inline");
        }

        if (tag.hasKey("Code"))
        {
            this.code = tag.getString("Code");
        }
    }
}