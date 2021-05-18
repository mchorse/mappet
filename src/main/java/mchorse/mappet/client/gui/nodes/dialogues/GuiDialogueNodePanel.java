package mchorse.mappet.client.gui.nodes.dialogues;

import mchorse.mappet.api.dialogues.nodes.DialogueNode;
import mchorse.mappet.client.gui.nodes.GuiEventNodePanel;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mclib.client.gui.framework.elements.input.GuiColorElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiDialogueNodePanel extends GuiEventNodePanel<DialogueNode>
{
    public GuiTextElement text;
    public GuiColorElement color;

    public GuiDialogueNodePanel(Minecraft mc)
    {
        super(mc);

        this.text = GuiMappetUtils.fullWindowContext(
            new GuiTextElement(mc, 10000, (text) -> this.node.message.text = text),
            IKey.lang("mappet.gui.nodes.dialogue.content")
        );
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