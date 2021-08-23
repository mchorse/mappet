package mchorse.mappet.client.gui.triggers.panels;

import mchorse.mappet.api.triggers.blocks.EventTriggerBlock;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.triggers.GuiTriggerOverlayPanel;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiEventTriggerBlockPanel extends GuiDataTriggerBlockPanel<EventTriggerBlock>
{
    public GuiEventTriggerBlockPanel(Minecraft mc, GuiTriggerOverlayPanel overlay, EventTriggerBlock block)
    {
        super(mc, overlay, block);

        this.addDelay();
    }

    @Override
    protected IKey getLabel()
    {
        return IKey.lang("mappet.gui.overlays.event");
    }

    @Override
    protected ContentType getType()
    {
        return ContentType.EVENT;
    }
}