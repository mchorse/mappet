package mchorse.mappet.client.gui.nodes.quests;

import mchorse.mappet.api.quests.chains.QuestNode;
import mchorse.mappet.client.gui.nodes.GuiNodePanel;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiQuestNodePanel extends GuiNodePanel<QuestNode>
{
    public GuiTextElement quest;
    public GuiTextElement receiver;

    public GuiQuestNodePanel(Minecraft mc)
    {
        super(mc);

        this.quest = new GuiTextElement(mc, 10000, (text) -> this.node.quest = text);
        this.receiver = new GuiTextElement(mc, 10000, (text) -> this.node.receiver = text);

        this.add(Elements.label(IKey.str("Quest")), this.quest);
        this.add(Elements.label(IKey.str("Receiver")).marginTop(12), this.receiver);
    }

    @Override
    public void set(QuestNode node)
    {
        super.set(node);

        this.quest.setText(node.quest);
        this.receiver.setText(node.receiver);
    }
}