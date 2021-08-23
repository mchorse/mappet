package mchorse.mappet.api.triggers.blocks;

import mchorse.mappet.Mappet;
import mchorse.mappet.api.dialogues.Dialogue;
import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.utils.DataContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class DialogueTriggerBlock extends DataTriggerBlock
{
    public DialogueTriggerBlock()
    {
        super();
    }

    public DialogueTriggerBlock(String string)
    {
        super(string);
    }

    @Override
    public void trigger(DataContext context)
    {
        if (!this.string.isEmpty())
        {
            EntityPlayer player = context.getPlayer();

            if (player instanceof EntityPlayerMP)
            {
                Dialogue dialogue = Mappet.dialogues.load(this.string);

                if (dialogue != null)
                {
                    Mappet.dialogues.open((EntityPlayerMP) player, dialogue, new DialogueContext(this.apply(context)));
                }
            }
        }
    }

    @Override
    protected String getKey()
    {
        return "Dialogue";
    }
}