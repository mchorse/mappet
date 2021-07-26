package mchorse.mappet.client.gui.nodes.dialogues;

import mchorse.mappet.api.dialogues.nodes.QuestChainNode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.nodes.GuiEventBaseNodePanel;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiQuestChainNodePanel extends GuiEventBaseNodePanel<QuestChainNode>
{
    public GuiButtonElement questChain;
    public GuiTextElement subject;

    public GuiQuestChainNodePanel(Minecraft mc)
    {
        super(mc);

        this.questChain = new GuiButtonElement(mc, IKey.lang("mappet.gui.overlays.chain"), (b) -> this.openQuestChains());
        this.subject = new GuiTextElement(mc, 1000, (t) -> this.node.subject = t);

        this.add(this.questChain);
        this.add(Elements.label(IKey.lang("mappet.gui.nodes.event.subject")).marginTop(12), this.subject);
    }

    private void openQuestChains()
    {
        GuiMappetUtils.openPicker(ContentType.CHAINS, this.node.chain, (name) -> this.node.chain = name);
    }

    @Override
    public void set(QuestChainNode node)
    {
        super.set(node);

        this.subject.setText(node.subject);
    }
}