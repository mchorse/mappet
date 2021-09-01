package mchorse.mappet.client.gui.nodes.dialogues;

import mchorse.mappet.api.dialogues.nodes.DialogueNode;
import mchorse.mappet.client.gui.nodes.GuiEventBaseNodePanel;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mappet.client.gui.utils.text.GuiMultiTextElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiColorElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiDialogueNodePanel extends GuiEventBaseNodePanel<DialogueNode>
{
    public GuiMultiTextElement text;
    public GuiColorElement color;

    public GuiDialogueNodePanel(Minecraft mc)
    {
        super(mc);

        this.text = new GuiMultiTextElement(mc, (text) -> this.node.message.text = text);
        this.text.background().flex().h(100);
        this.color = new GuiColorElement(mc, (c) -> this.node.message.color = c);

        this.add(Elements.label(IKey.lang("mappet.gui.nodes.dialogue.content")).marginTop(12), this.text, this.color);
    }

    @Override
    public void set(DialogueNode node)
    {
        super.set(node);

        this.text.setText(node.message.text);
        this.color.picker.setColor(node.message.color);
    }
}