package mchorse.mappet.api.events.nodes;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.EventContext;

public class EventNode extends DataNode
{
    @Override
    public int execute(EventContext context)
    {
        EventContext newContext = Mappet.events.execute(this.dataId, new EventContext(this.apply(context)));

        return this.booleanToExecutionCode(newContext.executions > 0);
    }
}