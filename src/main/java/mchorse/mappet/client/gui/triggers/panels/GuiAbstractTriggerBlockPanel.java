package mchorse.mappet.client.gui.triggers.panels;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.triggers.blocks.AbstractTriggerBlock;
import mchorse.mappet.client.gui.triggers.GuiTriggerOverlayPanel;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public abstract class GuiAbstractTriggerBlockPanel <T extends AbstractTriggerBlock> extends GuiElement
{
    public GuiTrackpadElement frequency;

    protected GuiTriggerOverlayPanel overlay;
    protected T block;

    public GuiAbstractTriggerBlockPanel(Minecraft mc, GuiTriggerOverlayPanel overlay, T block)
    {
        super(mc);

        this.overlay = overlay;
        this.block = block;

        this.frequency = new GuiTrackpadElement(mc, (v) -> this.block.frequency = v.intValue());
        this.frequency.limit(1).integer().setValue(block.frequency);

        GuiLabel label = Elements.label(IKey.lang("mappet.gui.trigger_types." + CommonProxy.getTriggerBlocks().getType(block)));

        this.flex().column(5).vertical().stretch();
        this.add(label);
    }

    public void addDelay()
    {
        this.add(Elements.label(IKey.lang("mappet.gui.triggers.frequency")).marginTop(12), this.frequency);
    }
}