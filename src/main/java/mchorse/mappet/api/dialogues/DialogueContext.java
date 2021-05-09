package mchorse.mappet.api.dialogues;

import mchorse.mappet.api.dialogues.nodes.CraftingNode;
import mchorse.mappet.api.dialogues.nodes.QuestChainNode;
import mchorse.mappet.api.dialogues.nodes.ReactionNode;
import mchorse.mappet.api.dialogues.nodes.ReplyNode;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.utils.DataContext;
import mchorse.mappet.api.utils.TriggerSender;
import net.minecraft.entity.EntityLivingBase;

import java.util.ArrayList;
import java.util.List;

public class DialogueContext extends EventContext
{
    public ReactionNode reactionNode;
    public List<ReplyNode> replyNodes = new ArrayList<ReplyNode>();
    public CraftingNode crafting;
    public QuestChainNode quest;

    public DialogueContext(DataContext data)
    {
        super(data);
    }

    public void reset()
    {
        this.reactionNode = null;
        this.replyNodes.clear();
        this.crafting = null;
        this.quest = null;
    }
}