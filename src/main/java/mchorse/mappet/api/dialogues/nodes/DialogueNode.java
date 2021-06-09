package mchorse.mappet.api.dialogues.nodes;

import mchorse.mappet.api.dialogues.DialogueFragment;
import mchorse.mappet.api.events.nodes.EventBaseNode;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.Constants;

public abstract class DialogueNode extends EventBaseNode
{
    public DialogueFragment message = new DialogueFragment();

    @Override
    protected String getDisplayTitle()
    {
        return this.message.getProcessedText();
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();
        NBTTagCompound message = this.message.serializeNBT();

        if (message.getSize() > 0)
        {
            tag.setTag("Message", message);
        }

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Message", Constants.NBT.TAG_COMPOUND))
        {
            this.message.deserializeNBT(tag.getCompoundTag("Message"));
        }
    }
}
