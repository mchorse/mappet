package mchorse.mappet.api.dialogues;

import mchorse.mappet.api.events.nodes.EventBaseNode;
import mchorse.mappet.api.triggers.Trigger;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import mchorse.mappet.api.utils.factory.IFactory;
import net.minecraft.nbt.NBTTagCompound;

public class Dialogue extends NodeSystem<EventBaseNode>
{
    public boolean closable = true;
    public Trigger onClose = new Trigger();

    public Dialogue(IFactory<EventBaseNode> factory)
    {
        super(factory);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        tag.setBoolean("Closable", this.closable);
        tag.setTag("OnClose", this.onClose.serializeNBT());

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Closable"))
        {
            this.closable = tag.getBoolean("Closable");
        }

        if (tag.hasKey("OnClose"))
        {
            this.onClose.deserializeNBT(tag.getCompoundTag("OnClose"));
        }
    }
}