package mchorse.mappet.api.dialogues.nodes;

import mchorse.mappet.api.events.EventContext;

public class CommentNode extends DialogueNode
{
    public CommentNode()
    {}

    public CommentNode(String message)
    {
        this.message.text = message;
    }

    @Override
    public int execute(EventContext context) {
        return 0;
    }
}