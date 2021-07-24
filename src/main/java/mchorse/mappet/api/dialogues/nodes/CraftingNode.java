package mchorse.mappet.api.dialogues.nodes;

import mchorse.mappet.api.dialogues.DialogueContext;
import mchorse.mappet.api.events.EventContext;
import mchorse.mappet.api.events.nodes.EventBaseNode;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CraftingNode extends EventBaseNode
{
    public String table = "";

    @Override
    public int execute(EventContext context)
    {
        ((DialogueContext) context).crafting = this;

        return EventBaseNode.HALT;
    }

    @Override
    @SideOnly(Side.CLIENT)
    protected String getDisplayTitle()
    {
        return this.table;
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