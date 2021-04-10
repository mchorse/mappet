package mchorse.mappet.api.dialogues;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.dialogues.nodes.ReactionNode;
import mchorse.mappet.api.dialogues.nodes.ReplyNode;
import mchorse.mappet.api.events.EventManager;
import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.BaseManager;
import mchorse.mappet.api.utils.nodes.factory.MapNodeFactory;

import java.io.File;
import java.util.List;

public class DialogueManager extends BaseManager<DialogueNodeSystem>
{
    public static final MapNodeFactory FACTORY = EventManager.FACTORY.copy()
        .register("reply", ReplyNode.class)
        .register("reaction", ReactionNode.class);

    public DialogueManager(File folder)
    {
        super(folder);
    }

    public DialogueContext execute(DialogueNodeSystem event, DialogueContext context)
    {
        if (event.main != null)
        {
            this.recursiveExecute(event, event.main, context, false);
        }

        return context;
    }

    public void recursiveExecute(DialogueNodeSystem system, EventNode node, DialogueContext context, boolean ignoreFirstNode)
    {
        if (context.executions >= Mappet.eventMaxExecutions.get())
        {
            return;
        }

        int result = ignoreFirstNode ? EventNode.ALL : node.execute(context);

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

    @Override
    public DialogueNodeSystem create()
    {
        return new DialogueNodeSystem(FACTORY);
    }
}