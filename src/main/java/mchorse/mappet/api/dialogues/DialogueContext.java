package mchorse.mappet.api.dialogues;

import mchorse.mappet.api.dialogues.nodes.ReactionNode;
import mchorse.mappet.api.dialogues.nodes.ReplyNode;
import mchorse.mappet.api.events.EventContext;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.server.MinecraftServer;

import java.util.ArrayList;
import java.util.List;

public class DialogueContext extends EventContext
{
    public ReactionNode reactionNode;
    public List<ReplyNode> replyNodes = new ArrayList<ReplyNode>();

    public DialogueContext(MinecraftServer server, EntityLivingBase subject)
    {
        super(server, subject);
    }

    public DialogueContext(MinecraftServer server, EntityLivingBase subject, boolean debug)
    {
        super(server, subject, debug);
    }

    public DialogueContext(MinecraftServer server, EntityLivingBase subject, EntityLivingBase object, boolean debug)
    {
        super(server, subject, object, debug);
    }
}