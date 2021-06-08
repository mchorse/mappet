package mchorse.mappet.client.gui.triggers.panels;

import mchorse.mappet.ClientProxy;
import mchorse.mappet.api.triggers.blocks.StringTriggerBlock;
import mchorse.mappet.api.utils.ContentType;
import mchorse.mappet.client.gui.triggers.GuiTriggerOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiContentNamesOverlayPanel;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public abstract class GuiStringTriggerBlockPanel <T extends StringTriggerBlock> extends GuiAbstractTriggerBlockPanel<T>
{
    public GuiButtonElement picker;

    public GuiStringTriggerBlockPanel(Minecraft mc, GuiTriggerOverlayPanel overlay, T block)
    {
        super(mc, overlay, block);

        this.picker = new GuiButtonElement(mc, this.getLabel(), (b) -> this.openOverlay());
        this.add(this.picker);
    }

    protected abstract IKey getLabel();

    protected abstract ContentType getType();

    protected void openOverlay()
    {
        ClientProxy.requestNames(this.getType(), (names) ->
        {
            GuiContentNamesOverlayPanel overlay = new GuiContentNamesOverlayPanel(this.mc, this.getLabel(), this.getType(), names, this::setString);

            overlay.set(this.block.string);
            GuiOverlay.addOverlay(GuiBase.getCurrent(), overlay, 0.5F, 0.7F);
        });
    }

    private void setString(String string)
    {
        this.block.string = string;
    }
}