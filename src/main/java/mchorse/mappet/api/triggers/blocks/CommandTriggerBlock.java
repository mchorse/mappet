package mchorse.mappet.api.triggers.blocks;

import mchorse.mappet.api.utils.DataContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CommandTriggerBlock extends StringTriggerBlock
{
    public CommandTriggerBlock()
    {
        super();
    }

    public CommandTriggerBlock(String string)
    {
        super(string);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String stringify()
    {
        if (this.string.startsWith("/"))
        {
            return "/" + this.string;
        }

        return this.string;
    }

    @Override
    public void trigger(DataContext context)
    {
        if (!this.string.isEmpty())
        {
            context.execute(this.string);
        }
    }

    @Override
    protected String getKey()
    {
        return "Command";
    }
}