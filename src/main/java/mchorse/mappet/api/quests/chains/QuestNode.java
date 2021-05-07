package mchorse.mappet.api.quests.chains;

import mchorse.mappet.api.utils.nodes.Node;
import net.minecraft.nbt.NBTTagCompound;

public class QuestNode extends Node
{
    public String quest = "";
    public String receiver = "";

    @Override
    public int getColor()
    {
        return 0xffff00;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        if (!this.quest.isEmpty())
        {
            tag.setString("Quest", this.quest);
        }

        if (!this.receiver.isEmpty())
        {
            tag.setString("Receiver", this.receiver);
        }

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

        if (tag.hasKey("Receiver"))
        {
            this.receiver = tag.getString("Receiver");
        }
    }
}