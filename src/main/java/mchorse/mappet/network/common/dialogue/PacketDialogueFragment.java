package mchorse.mappet.network.common.dialogue;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.Mappet;
import mchorse.mappet.api.crafting.CraftingTable;
import mchorse.mappet.api.dialogues.DialogueFragment;
import mchorse.metamorph.api.MorphUtils;
import mchorse.metamorph.api.morphs.AbstractMorph;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.List;

public class PacketDialogueFragment implements IMessage
{
    public String title = "";
    public AbstractMorph morph;
    public DialogueFragment reaction = new DialogueFragment();
    public List<DialogueFragment> replies = new ArrayList<DialogueFragment>();
    public CraftingTable table;

    public PacketDialogueFragment()
    {}

    public PacketDialogueFragment(String title, DialogueFragment reaction, List<DialogueFragment> replies)
    {
        this.title = title;
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

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.title = ByteBufUtils.readUTF8String(buf);
        this.morph = MorphUtils.morphFromBuf(buf);
        this.reaction.deserializeNBT(ByteBufUtils.readTag(buf));

        for (int i = 0, c = buf.readInt(); i < c; i++)
        {
            DialogueFragment fragment = new DialogueFragment();

            fragment.deserializeNBT(ByteBufUtils.readTag(buf));
            this.replies.add(fragment);
        }

        if (buf.readBoolean())
        {
            String id = ByteBufUtils.readUTF8String(buf);

            this.table = Mappet.crafting.create(id, ByteBufUtils.readTag(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.title);
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
    }
}