package mchorse.mappet.network.common.dialogue;

import io.netty.buffer.ByteBuf;
import mchorse.mappet.api.dialogues.DialogueFragment;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.ArrayList;
import java.util.List;

public class PacketDialogueFragment implements IMessage
{
    public String title = "";
    public DialogueFragment reaction = new DialogueFragment();
    public List<DialogueFragment> replies = new ArrayList<DialogueFragment>();

    public PacketDialogueFragment()
    {}

    public PacketDialogueFragment(String title, DialogueFragment reaction, List<DialogueFragment> replies)
    {
        this.title = title;
        this.reaction = reaction;
        this.replies = replies;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.title = ByteBufUtils.readUTF8String(buf);
        this.reaction.deserializeNBT(ByteBufUtils.readTag(buf));

        for (int i = 0, c = buf.readInt(); i < c; i++)
        {
            DialogueFragment fragment = new DialogueFragment();

            fragment.deserializeNBT(ByteBufUtils.readTag(buf));
            this.replies.add(fragment);
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        ByteBufUtils.writeUTF8String(buf, this.title);
        ByteBufUtils.writeTag(buf, this.reaction.serializeNBT());

        buf.writeInt(this.replies.size());

        for (DialogueFragment fragment : this.replies)
        {
            ByteBufUtils.writeTag(buf, fragment.serializeNBT());
        }
    }
}