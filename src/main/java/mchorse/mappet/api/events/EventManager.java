package mchorse.mappet.api.events;

import mchorse.mappet.api.utils.BaseManager;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.nodes.CommandNode;
import mchorse.mappet.api.events.nodes.ConditionNode;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import mchorse.mappet.api.utils.nodes.factory.MapNodeFactory;

import java.io.File;
import java.util.List;

public class EventManager extends BaseManager<NodeSystem<EventNode>>
{
    public static final MapNodeFactory factory = new MapNodeFactory()
        .register("command", CommandNode.class)
        .register("condition", ConditionNode.class);

    public EventManager(File folder)
    {
        super(folder);
    }

    /* Execution */

    public EventContext execute(NodeSystem<EventNode> event, EventContext context)
    {
        if (event.main != null)
        {
            this.recursiveExecute(event, event.main, context);
        }

        return context;
    }

    private void recursiveExecute(NodeSystem<EventNode> system, EventNode node, EventContext context)
    {
        if (context.executions >= Mappet.eventMaxExecutions.get())
        {
            return;
        }

        if (node.execute(context))
        {
            context.nesting += 1;

            List<EventNode> children = system.getChildren(node);

            for (EventNode child : children)
            {
                this.recursiveExecute(system, child, context);
            }

            context.nesting -= 1;
        }

        context.executions += 1;
    }

    @Override
    public NodeSystem<EventNode> create()
    {
        return new NodeSystem<EventNode>(factory);
    }
}