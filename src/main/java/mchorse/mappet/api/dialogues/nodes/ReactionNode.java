package mchorse.mappet.api.dialogues.nodes;

import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.events.EventContext;

public class ReactionNode extends DialogueNode
{
    public ReactionNode()
    {}

    public ReactionNode(String message)
    {
        this.message.text = message;
    }

    @Override
    public int execute(EventContext context)
    {
        if (context instanceof DialogueContext)
        {
            ((DialogueContext) context).reactionNode = this;
        }

        return 0;
    }
}