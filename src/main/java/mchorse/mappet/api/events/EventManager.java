package mchorse.mappet.api.events;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.nodes.EventBaseNode;
import mchorse.mappet.api.utils.manager.BaseManager;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.util.List;

public class EventManager extends BaseManager<NodeSystem<EventBaseNode>>
{
    public EventManager(File folder)
    {
        super(folder);
    }

    @Override
    protected NodeSystem<EventBaseNode> createData(String id, NBTTagCompound tag)
    {
        NodeSystem<EventBaseNode> event = new NodeSystem<EventBaseNode>(CommonProxy.getEvents());

        if (tag != null)
        {
            event.deserializeNBT(tag);
        }

        return event;
    }

    /* Execution */

    public EventContext execute(String id, EventContext context)
    {
        NodeSystem<EventBaseNode> event = this.load(id);

        if (event != null)
        {
            this.execute(event, context);
        }

        return context;
    }

    public EventContext execute(NodeSystem<EventBaseNode> event, EventContext context)
    {
        if (event.main != null)
        {
            context.system = event;

            this.recursiveExecute(event, event.main, context, false);
            context.submitDelayedExecutions();
        }

        return context;
    }

    public void recursiveExecute(NodeSystem<EventBaseNode> system, EventBaseNode node, EventContext context, boolean skipFirst)
    {
        if (context.executions >= Mappet.eventMaxExecutions.get())
        {
            return;
        }

        int result = skipFirst ? EventBaseNode.ALL : node.execute(context);

        if (result >= EventBaseNode.ALL)
        {
            context.nesting += 1;

            List<EventBaseNode> children = system.getChildren(node);

            if (result == EventBaseNode.ALL)
            {
                for (EventBaseNode child : children)
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