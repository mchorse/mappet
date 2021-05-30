package mchorse.mappet.api.events;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.events.hotkeys.EventHotkeys;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.manager.BaseManager;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import net.minecraft.nbt.NBTTagCompound;

import java.io.File;
import java.util.List;

public class EventManager extends BaseManager<NodeSystem<EventNode>>
{
    public EventHotkeys hotkeys = new EventHotkeys();

    public EventManager(File folder)
    {
        super(folder);
    }

    @Override
    protected NodeSystem<EventNode> createData(String id, NBTTagCompound tag)
    {
        NodeSystem<EventNode> event = new NodeSystem<EventNode>(CommonProxy.getEvents());

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