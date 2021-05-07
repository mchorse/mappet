package mchorse.mappet.client.gui.nodes.dialogues;

import mchorse.mappet.api.dialogues.nodes.QuestChainNode;
import mchorse.mappet.client.gui.nodes.GuiEventNodePanel;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiQuestChainNodePanel extends GuiEventNodePanel<QuestChainNode>
{
    public GuiTextElement questChain;

    public GuiQuestChainNodePanel(Minecraft mc)
    {
        super(mc);

        this.questChain = new GuiTextElement(mc, 10000, (text) -> this.node.chain = text);

        this.add(Elements.label(IKey.str("Quest chain")), this.questChain);
    }

    @Override
    public void set(QuestChainNode node)
    {
        super.set(node);

        this.questChain.setText(node.chain);
    }
}