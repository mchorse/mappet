package mchorse.mappet.api.dialogues.nodes;

import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.events.nodes.EventNode;
import net.minecraft.nbt.NBTTagCompound;

public class CraftingNode extends EventNode
{
    public String table = "";

    @Override
    public int execute(EventContext context)
    {
        ((DialogueContext) context).crafting = this;

        return EventNode.HALT;
    }

    @Override
    public int getColor()
    {
        return 0xff5e00;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        tag.setString("CraftingTable", this.table);

        return tag;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag)
    {
        super.deserializeNBT(tag);

        if (tag.hasKey("CraftingTable"))
        {
            this.table = tag.getString("CraftingTable");
        }
    }
}