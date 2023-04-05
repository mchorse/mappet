package mchorse.mappet.client.gui.triggers.panels;

import mchorse.mappet.api.triggers.blocks.DialogueTriggerBlock;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.triggers.GuiTriggerOverlayPanel;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiDialogueTriggerBlockPanel extends GuiDataTriggerBlockPanel<DialogueTriggerBlock>
{
    public GuiDialogueTriggerBlockPanel(Minecraft mc, GuiTriggerOverlayPanel overlay, DialogueTriggerBlock block)
    {
        super(mc, overlay, block);
        this.addPicker();
        this.addData();
        this.addDelay();
    }

    @Override
    protected IKey getLabel()
    {
        return IKey.lang("mappet.gui.overlays.dialogue");
    }

    @Override
    protected ContentType getType()
    {
        return ContentType.DIALOGUE;
    }
}