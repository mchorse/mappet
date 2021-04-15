package mchorse.mappet.client.gui.nodes;

import mchorse.mappet.api.dialogues.nodes.DialogueNode;
import mchorse.mappet.api.events.nodes.CommandNode;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiDialogueNodePanel extends GuiNodePanel<DialogueNode>
{
    public GuiTextElement text;

    public GuiDialogueNodePanel(Minecraft mc)
    {
        super(mc);

        this.text = new GuiTextElement(mc, 10000, (text) -> this.node.message.text = text);

        this.add(Elements.label(IKey.str("Content")), this.text);
    }

    @Override
    public void set(DialogueNode node)
    {
        super.set(node);

        this.text.setText(node.message.text);
    }
}