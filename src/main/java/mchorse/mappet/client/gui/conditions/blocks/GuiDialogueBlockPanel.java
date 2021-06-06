package mchorse.mappet.client.gui.conditions.blocks;

import mchorse.mappet.ClientProxy;
import mchorse.mappet.api.conditions.blocks.DialogueBlock;
import mchorse.mappet.api.conditions.blocks.TargetBlock;
import mchorse.mappet.api.conditions.utils.Target;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.conditions.GuiConditionOverlayPanel;
import mchorse.mappet.client.gui.conditions.utils.GuiTargetBlockElement;
import mchorse.mappet.client.gui.utils.overlays.GuiContentNamesOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiDialogueBlockPanel extends GuiAbstractBlockPanel<DialogueBlock>
{
    public GuiButtonElement id;
    public GuiTextElement marker;
    public GuiTargetBlockElement<TargetBlock> property;

    public GuiDialogueBlockPanel(Minecraft mc, GuiConditionOverlayPanel overlay, DialogueBlock block)
    {
        super(mc, overlay, block);

        this.id = new GuiButtonElement(mc, IKey.lang("mappet.gui.overlays.dialogue"), (t) -> this.openDialogues());
        this.marker = new GuiTextElement(mc, 1000, (t) -> this.block.marker = t);
        this.marker.setText(block.marker);
        this.property = new GuiTargetBlockElement<TargetBlock>(mc, block).skipGlobal().skip(Target.NPC);

        this.add(Elements.row(mc, 5,
            Elements.column(mc, 5, Elements.label(IKey.lang("mappet.gui.conditions.dialogue.id")).marginTop(12), this.id),
            Elements.column(mc, 5, Elements.label(IKey.lang("mappet.gui.conditions.dialogue.marker")).marginTop(12), this.marker)
        ));
        this.add(this.property.targeter.marginTop(12));
    }

    private void openDialogues()
    {
        ClientProxy.requestNames(ContentType.DIALOGUE, (names) ->
        {
            GuiContentNamesOverlayPanel overlay = new GuiContentNamesOverlayPanel(this.mc, IKey.lang("mappet.gui.overlays.dialogue"), ContentType.DIALOGUE, names, (name) -> this.block.id = name);

            overlay.set(this.block.id);
            GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.7F);
        });
    }
}