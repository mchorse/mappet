package mchorse.mappet.client.gui.triggers.panels;

import mchorse.mappet.CommonProxy;
import mchorse.mappet.api.triggers.blocks.AbstractTriggerBlock;
import mchorse.mappet.client.gui.triggers.GuiTriggerOverlayPanel;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public abstract class GuiAbstractTriggerBlockPanel <T extends AbstractTriggerBlock> extends GuiElement
{
    protected GuiTriggerOverlayPanel overlay;
    protected T block;

    public GuiAbstractTriggerBlockPanel(Minecraft mc, GuiTriggerOverlayPanel overlay, T block)
    {
        super(mc);

        this.overlay = overlay;
        this.block = block;

        GuiLabel label = Elements.label(IKey.lang("mappet.gui.trigger_types." + CommonProxy.getTriggerBlocks().getType(block)));

        this.flex().column(5).vertical().stretch();
        this.add(label);
    }
}