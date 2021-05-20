package mchorse.mappet.api.dialogues.nodes;

import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.events.nodes.EventNode;
import net.minecraft.nbt.NBTTagCompound;

public class QuestChainNode extends EventNode
{
    public String chain = "";

    @Override
    public int execute(EventContext context)
    {
        ((DialogueContext) context).quest = this;

        return EventNode.HALT;
    }

    @Override
    protected String getDisplayTitle()
    {
        return this.chain;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        tag.setString("Chain", this.chain);

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
    }
}