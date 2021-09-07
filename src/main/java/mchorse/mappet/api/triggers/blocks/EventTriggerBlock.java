package mchorse.mappet.api.triggers.blocks;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.utils.DataContext;

public class EventTriggerBlock extends DataTriggerBlock
{
    public EventTriggerBlock()
    {
        super();
    }

    public EventTriggerBlock(String string)
    {
        super(string);
    }

    @Override
    public void trigger(DataContext context)
    {
        if (!this.string.isEmpty())
        {
            DataContext data = this.apply(context);

            Mappet.events.execute(this.string, new EventContext(data));

            if (!context.isCanceled())
            {
                context.cancel(data.isCanceled());
            }
        }
    }

    @Override
    protected String getKey()
    {
        return "Event";
    }
}