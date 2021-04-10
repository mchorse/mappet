package mchorse.mappet.api.dialogues;

import mchorse.mappet.api.events.nodes.EventNode;
import mchorse.mappet.api.utils.nodes.NodeSystem;
import mchorse.mappet.api.utils.nodes.factory.INodeFactory;
import net.minecraft.nbt.NBTTagCompound;

public class DialogueNodeSystem extends NodeSystem<EventNode>
{
    public String title = "";

    public DialogueNodeSystem(INodeFactory<EventNode> factory)
    {
        super(factory);
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        tag.setString("Title", this.title);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("Title"))
        {
            this.title = tag.getString("Title");
        }
    }
}