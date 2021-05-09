package mchorse.mappet.api.quests.chains;

import mchorse.mappet.api.utils.nodes.Node;
import net.minecraft.nbt.NBTTagCompound;

public class QuestNode extends Node
{
    public String quest = "";
    public String giver = "";
    public String receiver = "";

    @Override
    public int getColor()
    {
        return 0xffff00;
    }

    @Override
    protected String getDisplayTitle()
    {
        return this.quest;
    }

    @Override
    public NBTTagCompound serializeNBT()
    {
        NBTTagCompound tag = super.serializeNBT();

        if (!this.quest.isEmpty())
        {
            tag.setString("Quest", this.quest);
        }

        if (!this.giver.isEmpty())
        {
            tag.setString("Giver", this.giver);
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

        if (tag.hasKey("Giver"))
        {
            this.giver = tag.getString("Giver");
        }

        if (tag.hasKey("Receiver"))
        {
            this.receiver = tag.getString("Receiver");
        }
    }
}