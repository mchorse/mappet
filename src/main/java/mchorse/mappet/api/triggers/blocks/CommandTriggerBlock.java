package mchorse.mappet.api.triggers.blocks;

import mchorse.mappet.api.utils.DataContext;

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