package mchorse.mappet.api.dialogues.nodes;

import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.events.nodes.EventBaseNode;

public class CommentNode extends DialogueNode
{
    public CommentNode()
    {}

    public CommentNode(String message)
    {
        this.message.text = message;
    }

    @Override
    public int execute(EventContext context)
    {
        return EventBaseNode.ALL;
    }
}