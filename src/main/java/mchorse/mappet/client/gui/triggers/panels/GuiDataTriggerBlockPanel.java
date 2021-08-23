package mchorse.mappet.client.gui.triggers.panels;

import mchorse.mappet.api.triggers.blocks.DataTriggerBlock;
import mchorse.mappet.client.gui.triggers.GuiTriggerOverlayPanel;
import mchorse.mappet.client.gui.utils.GuiMappetUtils;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public abstract class GuiDataTriggerBlockPanel <T extends DataTriggerBlock> extends GuiStringTriggerBlockPanel<T>
{
    public GuiTextElement data;

    public GuiDataTriggerBlockPanel(Minecraft mc, GuiTriggerOverlayPanel overlay, T block)
    {
        super(mc, overlay, block);

        this.data = GuiMappetUtils.fullWindowContext(
            new GuiTextElement(mc, 100000, (text) -> this.block.customData = text),
            IKey.lang("mappet.gui.nodes.event.data")
        );
        this.data.tooltip(IKey.lang("mappet.gui.nodes.event.data_tooltip"));
        this.data.setText(block.customData);
        this.add(Elements.label(IKey.lang("mappet.gui.nodes.event.data")).marginTop(12), this.data);
    }
}