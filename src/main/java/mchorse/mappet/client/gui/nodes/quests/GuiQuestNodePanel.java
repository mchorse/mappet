package mchorse.mappet.client.gui.nodes.quests;

import mchorse.mappet.ClientProxy;
import mchorse.mappet.api.quests.chains.QuestNode;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.nodes.GuiNodePanel;
import mchorse.mappet.client.gui.utils.overlays.GuiContentNamesOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiQuestNodePanel extends GuiNodePanel<QuestNode>
{
    public GuiButtonElement quest;
    public GuiTextElement giver;
    public GuiTextElement receiver;
    public GuiToggleElement autoAccept;
    public GuiToggleElement allowRetake;

    public GuiQuestNodePanel(Minecraft mc)
    {
        super(mc);

        this.quest = new GuiButtonElement(mc, IKey.lang("mappet.gui.overlays.quest"), (b) -> this.openQuests());
        this.giver = new GuiTextElement(mc, 10000, (text) -> this.node.giver = text);
        this.receiver = new GuiTextElement(mc, 10000, (text) -> this.node.receiver = text);
        this.autoAccept = new GuiToggleElement(mc, IKey.lang("mappet.gui.nodes.dialogue.auto_accept"), (b) -> this.node.autoAccept = b.isToggled());
        this.allowRetake = new GuiToggleElement(mc, IKey.lang("mappet.gui.nodes.dialogue.allow_retake"), (b) -> this.node.allowRetake = b.isToggled());

        this.add(this.quest);
        this.add(Elements.label(IKey.lang("mappet.gui.nodes.dialogue.provider")).marginTop(12), this.giver);
        this.add(Elements.label(IKey.lang("mappet.gui.nodes.dialogue.receiver")).marginTop(12), this.receiver);
        this.add(this.autoAccept, this.allowRetake);
    }

    private void openQuests()
    {
        ClientProxy.requestNames(ContentType.QUEST, (names) ->
        {
            GuiContentNamesOverlayPanel overlay = new GuiContentNamesOverlayPanel(this.mc, IKey.lang("mappet.gui.overlays.quest"), ContentType.QUEST, names, (name) -> this.node.quest = name);

            overlay.set(this.node.quest);
            GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.7F);
        });
    }

    @Override
    public void set(QuestNode node)
    {
        super.set(node);

        this.giver.setText(node.giver);
        this.receiver.setText(node.receiver);
        this.autoAccept.toggled(node.autoAccept);
        this.allowRetake.toggled(node.allowRetake);
    }
}