package mchorse.mappet.api.dialogues;

import mchorse.mappet.api.dialogues.nodes.ReactionNode;
import mchorse.mappet.api.dialogues.nodes.ReplyNode;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.utils.TriggerSender;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class DialogueContext extends EventContext
{
    public ReactionNode reactionNode;
    public List<ReplyNode> replyNodes = new ArrayList<ReplyNode>();

    public DialogueContext(TriggerSender sender, EntityLivingBase subject)
    {
        super(sender, subject);
    }

    public DialogueContext(TriggerSender sender, EntityLivingBase subject, boolean debug)
    {
        super(sender, subject, debug);
    }

    public DialogueContext(TriggerSender sender, EntityLivingBase subject, EntityLivingBase object, boolean debug)
    {
        super(sender, subject, object, debug);
    }
}