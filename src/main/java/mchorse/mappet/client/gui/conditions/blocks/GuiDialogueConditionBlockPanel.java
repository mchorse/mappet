package mchorse.mappet.client.gui.conditions.blocks;

import mchorse.mappet.api.conditions.blocks.DialogueConditionBlock;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.api.utils.TargetMode;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mappet.client.gui.utils.GuiTargetElement;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiDialogueConditionBlockPanel extends GuiAbstractConditionBlockPanel<DialogueConditionBlock>
{
    public GuiButtonElement id;
    public GuiTextElement marker;
    public GuiTargetElement target;

    public GuiDialogueConditionBlockPanel(Minecraft mc, GuiConditionOverlayPanel overlay, DialogueConditionBlock block)
    {
        super(mc, overlay, block);

        this.id = new GuiButtonElement(mc, IKey.lang("mappet.gui.overlays.dialogue"), (t) -> this.openDialogues());
        this.marker = new GuiTextElement(mc, 1000, (t) -> this.block.marker = t);
        this.marker.setText(block.marker);
        this.target = new GuiTargetElement(mc, block.target).skipGlobal().skip(TargetMode.NPC);

        this.add(Elements.row(mc, 5,
            Elements.column(mc, 5, Elements.label(IKey.lang("mappet.gui.conditions.dialogue.id")).marginTop(12), this.id),
            Elements.column(mc, 5, Elements.label(IKey.lang("mappet.gui.conditions.dialogue.marker")).marginTop(12), this.marker)
        ));
        this.add(this.target.marginTop(12));
    }

    private void openDialogues()
    {
        GuiMappetUtils.openPicker(ContentType.DIALOGUE, this.block.id, (name) -> this.block.id = name);
    }
}