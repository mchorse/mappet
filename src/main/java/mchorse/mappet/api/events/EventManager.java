package mchorse.mappet.api.events;

import mchorse.mappet.api.events.nodes.SwitchNode;
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
    public static final MapNodeFactory FACTORY = new MapNodeFactory()
        .register("command", CommandNode.class)
        .register("condition", ConditionNode.class)
        .register("switch", SwitchNode.class);

    public EventManager(File folder)
    {
        super(folder);
    }

    /* Execution */

    public EventContext execute(String id, EventContext context)
    {
        NodeSystem<EventNode> event = this.load(id);

        if (event != null)
        {
            this.execute(event, context);
        }

        return context;
    }

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

        int result = node.execute(context);

        if (result >= EventNode.ALL)
        {
            context.nesting += 1;

            List<EventNode> children = system.getChildren(node);

            if (result == EventNode.ALL)
            {
                for (EventNode child : children)
                {
                    this.recursiveExecute(system, child, context);
                }
            }
            else if (result <= children.size())
            {
                this.recursiveExecute(system, children.get(result - 1), context);
            }

            context.nesting -= 1;
        }

        context.executions += 1;
    }

    @Override
    public NodeSystem<EventNode> create()
    {
        return new NodeSystem<EventNode>(FACTORY);
    }
}