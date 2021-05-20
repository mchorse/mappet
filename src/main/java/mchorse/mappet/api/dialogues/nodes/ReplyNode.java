package mchorse.mappet.api.dialogues.nodes;

import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.events.EventContext;

public class ReplyNode extends DialogueNode
{
    public ReplyNode()
    {}

    public ReplyNode(String message)
    {
        this.message.text = message;
    }

    @Override
    public int execute(EventContext context)
    {
        if (context instanceof DialogueContext)
        {
            ((DialogueContext) context).replyNodes.add(this);
        }

        return -1;
    }
}