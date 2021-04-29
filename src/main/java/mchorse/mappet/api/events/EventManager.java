package mchorse.mappet.api.events;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.nodes.CommandNode;
import mchorse.mappet.api.events.nodes.ConditionNode;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.events.nodes.SwitchNode;
import mchorse.mappet.api.events.nodes.TimerNode;
import mchorse.mappet.api.utils.BaseManager;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import mchorse.mappet.api.utils.nodes.factory.MapNodeFactory;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.util.List;

public class EventManager extends BaseManager<NodeSystem<EventNode>>
{
    public static final MapNodeFactory FACTORY = new MapNodeFactory()
        .register("command", CommandNode.class)
        .register("condition", ConditionNode.class)
        .register("switch", SwitchNode.class)
        .register("timer", TimerNode.class);

    public EventManager(File folder)
    {
        super(folder);
    }

    @Override
    public NodeSystem<EventNode> create(String id, NBTTagCompound tag)
    {
        NodeSystem<EventNode> event = new NodeSystem<EventNode>(FACTORY);

        if (tag != null)
        {
            event.deserializeNBT(tag);
        }

        return event;
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
            context.system = event;

            this.recursiveExecute(event, event.main, context, false);
            context.submitDelayedExecutions();
        }

        return context;
    }

    public void recursiveExecute(NodeSystem<EventNode> system, EventNode node, EventContext context, boolean skipFirst)
    {
        if (context.executions >= Mappet.eventMaxExecutions.get())
        {
            return;
        }

        int result = skipFirst ? EventNode.ALL : node.execute(context);

        if (result >= EventNode.ALL)
        {
            context.nesting += 1;

            List<EventNode> children = system.getChildren(node);

            if (result == EventNode.ALL)
            {
                for (EventNode child : children)
                {
                    this.recursiveExecute(system, child, context, false);
                }
            }
            else if (result <= children.size())
            {
                this.recursiveExecute(system, children.get(result - 1), context, false);
            }

            context.nesting -= 1;
        }

        context.executions += 1;
    }
}