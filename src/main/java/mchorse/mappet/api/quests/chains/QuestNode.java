package mchorse.mappet.api.quests.chains;

import mchorse.mappet.api.conditions.Checker;
import mchorse.mappet.api.utils.nodes.Node;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class QuestNode extends Node
{
    public String quest = "";
    public String giver = "";
    public String receiver = "";

    public boolean autoAccept;
    public boolean allowRetake;
    public Checker condition = new Checker(true);

    @Override
    @SideOnly(Side.CLIENT)
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

        if (this.autoAccept)
        {
            tag.setBoolean("AutoAccept", this.autoAccept);
        }

        if (this.allowRetake)
        {
            tag.setBoolean("AllowRetake", this.allowRetake);
        }

        tag.setTag("Condition", this.condition.serializeNBT());

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

        if (tag.hasKey("AutoAccept"))
        {
            this.autoAccept = tag.getBoolean("AutoAccept");
        }

        if (tag.hasKey("AllowRetake"))
        {
            this.allowRetake = tag.getBoolean("AllowRetake");
        }

        this.condition.deserializeNBT(tag.getCompoundTag("Condition"));
    }
}