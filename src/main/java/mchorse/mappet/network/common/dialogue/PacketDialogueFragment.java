package mchorse.mappet.network.common.dialogue;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.api.dialogues.DialogueFragment;
import mchorse.mappet.api.quests.chains.QuestContext;
import mchorse.mappet.api.quests.chains.QuestInfo;
import mchorse.mclib.utils.NBTUtils;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.List;

public class PacketDialogueFragment implements IMessage
{
    public String title = "";
    public boolean closable;
    public AbstractMorph morph;
    public DialogueFragment reaction = new DialogueFragment();
    public List<DialogueFragment> replies = new ArrayList<DialogueFragment>();
    public CraftingTable table;
    public boolean hasQuests;
    public boolean singleQuest;
    public List<QuestInfo> quests = new ArrayList<QuestInfo>();

    public PacketDialogueFragment()
    {}

    public PacketDialogueFragment(boolean closable, DialogueFragment reaction, List<DialogueFragment> replies)
    {
        this.closable = closable;
        this.reaction = reaction;
        this.replies = replies;
    }

    public void addMorph(AbstractMorph morph)
    {
        this.morph = morph;
    }

    public void addCraftingTable(CraftingTable table)
    {
        this.table = table;
    }

    public void addQuests(QuestContext context)
    {
        this.hasQuests = true;
        this.quests.addAll(context.quests);
    }

    public void addQuest(QuestInfo questInfo)
    {
        this.hasQuests = true;
        this.singleQuest = true;
        this.quests.add(questInfo);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.title = ByteBufUtils.readUTF8String(buf);
        this.closable = buf.readBoolean();
        this.morph = MorphUtils.morphFromBuf(buf);
        this.reaction.deserializeNBT(NBTUtils.readInfiniteTag(buf));

        for (int i = 0, c = buf.readInt(); i < c; i++)
        {
            DialogueFragment fragment = new DialogueFragment();

            fragment.deserializeNBT(NBTUtils.readInfiniteTag(buf));
            this.replies.add(fragment);
        }

        if (buf.readBoolean())
        {
            String id = ByteBufUtils.readUTF8String(buf);

            this.table = Mappet.crafting.create(id, NBTUtils.readInfiniteTag(buf));
        }

        this.hasQuests = buf.readBoolean();
        this.singleQuest = buf.readBoolean();

        for (int i = 0, c = buf.readInt(); i < c; i++)
        {
            QuestInfo info = new QuestInfo();

            info.fromBytes(buf);
            this.quests.add(info);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.title);
        buf.writeBoolean(this.closable);
        MorphUtils.morphToBuf(buf, this.morph);
        ByteBufUtils.writeTag(buf, this.reaction.serializeNBT());

        buf.writeInt(this.replies.size());

        for (DialogueFragment fragment : this.replies)
        {
            ByteBufUtils.writeTag(buf, fragment.serializeNBT());
        }

        buf.writeBoolean(this.table != null);

        if (this.table != null)
        {
            ByteBufUtils.writeUTF8String(buf, this.table.getId());
            ByteBufUtils.writeTag(buf, this.table.serializeNBT());
        }

        buf.writeBoolean(this.hasQuests);
        buf.writeBoolean(this.singleQuest);
        buf.writeInt(this.quests.size());

        for (QuestInfo info : this.quests)
        {
            info.toBytes(buf);
        }
    }

    public boolean isEmpty()
    {
        return this.replies.isEmpty() && !this.hasQuests && this.table == null;
    }
}