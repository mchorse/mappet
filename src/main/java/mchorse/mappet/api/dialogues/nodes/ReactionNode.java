package mchorse.mappet.api.dialogues.nodes;

import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.events.EventContext;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraft.nbt.NBTTagCompound;

public class ReactionNode extends DialogueNode
{
    public AbstractMorph morph;
    public boolean read;
    public String marker = "";

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

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        if (this.morph != null)
        {
            tag.setTag("Morph", this.morph.toNBT());
        }

        tag.setBoolean("Read", this.read);
        tag.setString("Marker", this.marker);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Morph"))
        {
            this.morph = MorphManager.INSTANCE.morphFromNBT(tag.getCompoundTag("Morph"));
        }

        this.read = tag.getBoolean("Read");
        this.marker = tag.getString("Marker");
    }
}