package mchorse.mappet.client.gui.conditions.blocks;

import mchorse.mappet.api.conditions.blocks.QuestConditionBlock;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.api.utils.TargetMode;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mappet.client.gui.utils.GuiTargetElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiCirculateElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiQuestConditionBlockPanel extends GuiAbstractConditionBlockPanel<QuestConditionBlock>
{
    public GuiButtonElement id;
    public GuiTargetElement target;
    public GuiCirculateElement quest;

    public GuiQuestConditionBlockPanel(Minecraft mc, GuiConditionOverlayPanel overlay, QuestConditionBlock block)
    {
        super(mc, overlay, block);

        this.id = new GuiButtonElement(mc, IKey.lang("mappet.gui.overlays.quest"), (t) -> this.openQuests());
        this.target = new GuiTargetElement(mc, block.target).skip(TargetMode.NPC);
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
        this.add(this.target.marginTop(12));
    }

    private void openQuests()
    {
        GuiMappetUtils.openPicker(ContentType.QUEST, this.block.id, (name) -> this.block.id = name);
    }

    private void toggleQuest(GuiCirculateElement b)
    {
        this.block.quest = QuestConditionBlock.QuestCheck.values()[b.getValue()];
    }
}