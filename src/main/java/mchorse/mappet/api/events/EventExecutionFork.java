package mchorse.mappet.api.events;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.nodes.EventBaseNode;
import mchorse.mappet.api.utils.IExecutable;
import mchorse.mappet.api.utils.nodes.NodeSystem;

public class EventExecutionFork implements IExecutable
{
    public NodeSystem<EventBaseNode> event;
    public EventBaseNode node;
    public EventContext context;
    public int timer;

    public EventExecutionFork(NodeSystem<EventBaseNode> event, EventBaseNode node, EventContext context, int timer)
    {
        this.event = event;
        this.node = node;
        this.context = context;
        this.timer = timer;
    }

    @Override
    public String getId()
    {
        return this.event.getId();
    }

    @Override
    public boolean update()
    {
        if (this.timer <= 0)
        {
            Mappet.events.recursiveExecute(this.event, this.node, this.context, true);

            this.context.submitDelayedExecutions();

            return true;
        }

        this.timer -= 1;

        return false;
    }
}