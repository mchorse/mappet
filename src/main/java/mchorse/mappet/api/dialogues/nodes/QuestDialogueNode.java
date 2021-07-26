package mchorse.mappet.api.dialogues.nodes;

import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.events.nodes.EventBaseNode;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class QuestDialogueNode extends EventBaseNode
{
    public String quest = "";

    @Override
    public int execute(EventContext context)
    {
        if (context instanceof DialogueContext)
        {
            ((DialogueContext) context).setQuest(this);
        }

        return EventBaseNode.HALT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String getDisplayTitle()
    {
        return this.quest;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        tag.setString("Quest", this.quest);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Quest"))
        {
            this.quest = tag.getString("Quest");
        }
    }
}
