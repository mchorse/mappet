package mchorse.mappet.client.gui.conditions.blocks;

import mchorse.mappet.ClientProxy;
import mchorse.mappet.api.conditions.blocks.QuestConditionBlock;
import mchorse.mappet.api.conditions.blocks.TargetConditionBlock;
import mchorse.mappet.api.conditions.utils.Target;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mappet.client.gui.conditions.utils.GuiTargetBlockElement;
import mchorse.mappet.client.gui.utils.overlays.GuiContentNamesOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiQuestConditionBlockPanel extends GuiAbstractConditionBlockPanel<QuestConditionBlock>
{
    public GuiButtonElement id;
    public GuiTargetBlockElement<TargetConditionBlock> property;
    public GuiCirculateElement quest;

    public GuiQuestConditionBlockPanel(Minecraft mc, GuiConditionOverlayPanel overlay, QuestConditionBlock block)
    {
        super(mc, overlay, block);

        this.id = new GuiButtonElement(mc, IKey.lang("mappet.gui.overlays.quest"), (t) -> this.openQuests());
        this.property = new GuiTargetBlockElement<TargetConditionBlock>(mc, block).skip(Target.NPC);
        this.quest = new GuiCirculateElement(mc, this::toggleQuest);

        for (QuestConditionBlock.QuestCheck check : QuestConditionBlock.QuestCheck.values())
        {
            this.quest.addLabel(IKey.lang("mappet.gui.conditions.quest.types." + check.name().toLowerCase()));
        }

        this.quest.setValue(block.quest.ordinal());

        this.add(Elements.row(mc, 5,
            Elements.column(mc, 5, Elements.label(IKey.lang("mappet.gui.conditions.quest.id")).marginTop(12), this.id),
            Elements.column(mc, 5, Elements.label(IKey.lang("mappet.gui.conditions.quest.check")).marginTop(12), this.quest)
        ));
        this.add(this.property.targeter.marginTop(12));
    }

    private void openQuests()
    {
        ClientProxy.requestNames(ContentType.QUEST, (names) ->
        {
            GuiContentNamesOverlayPanel overlay = new GuiContentNamesOverlayPanel(this.mc, IKey.lang("mappet.gui.overlays.quest"), ContentType.QUEST, names, (name) -> this.block.id = name);

            overlay.set(this.block.id);
            GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.7F);
        });
    }

    private void toggleQuest(GuiButtonElement b)
    {
        this.block.quest = QuestConditionBlock.QuestCheck.values()[this.quest.getValue()];
    }
}