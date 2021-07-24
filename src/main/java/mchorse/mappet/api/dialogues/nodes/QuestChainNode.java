package mchorse.mappet.api.dialogues.nodes;

import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.events.nodes.EventBaseNode;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class QuestChainNode extends EventBaseNode
{
    public String chain = "";
    public String subject = "";

    @Override
    public int execute(EventContext context)
    {
        ((DialogueContext) context).quest = this;

        return EventBaseNode.HALT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String getDisplayTitle()
    {
        return this.chain;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        tag.setString("Chain", this.chain);
        tag.setString("Subject", this.subject.trim());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Chain"))
        {
            this.chain = tag.getString("Chain");
        }

        if (tag.hasKey("Subject"))
        {
            this.subject = tag.getString("Subject");
        }
    }
}