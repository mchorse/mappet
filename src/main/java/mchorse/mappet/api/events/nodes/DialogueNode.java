package mchorse.mappet.api.events.nodes;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.dialogues.Dialogue;
import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.events.EventContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class DialogueNode extends DataNode
{
    @Override
    public int execute(EventContext context)
    {
        Dialogue dialogue = Mappet.dialogues.load(this.dataId);
        EntityPlayer player = context.data.getPlayer();
        boolean result = dialogue != null && player instanceof EntityPlayerMP;

        if (result)
        {
            Mappet.dialogues.open((EntityPlayerMP) player, dialogue, new DialogueContext(this.apply(context)));
        }

        return this.booleanToExecutionCode(result);
    }
}