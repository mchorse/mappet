package mchorse.mappet.api.events.nodes;

import mchorse.mappet.api.events.EventContext;

public class CancelNode extends EventBaseNode
{
    @Override
    public int execute(EventContext context)
    {
        context.data.canceled = true;

        return EventBaseNode.HALT;
    }
}